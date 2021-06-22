package com.example.moneymanagement.UI.BaseFragment.HomeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.item_home_card.view.*
import kotlinx.android.synthetic.main.item_home_last_transaction.view.*

private const val CARD = 0
private const val LAST_TRANSACTION = 1
private const val STATISTIC = 2

class LayoutAdapter(private var listLayout: List<String>, private val viewModel: UserViewModel, private val fragment: Fragment, private val listener: Listener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Listener {
        fun onCardClick(saldo: TextView, pemasukan: TextView, pengeluaran: TextView)
        fun onTransactionViewClick(transactionEntity: TransactionEntity)
    }

    inner class CardHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(viewModel: UserViewModel, fragment: Fragment, listener: Listener) {
            with(itemView) {
                loadUserSaldo(viewModel, this, fragment)
                itemView.setOnClickListener {
                    listener.onCardClick(current_saldo, pemasukan, pengeluaran)
                }
            }
        }
        fun loadUserSaldo(viewModel: UserViewModel, view: View, fragment: Fragment) {
            with(view) {
                viewModel.getCurrentSaldo()?.let {
                    HomeFragment.saldo_user = it
                    current_saldo.text = Utilities.formatNumber(it)
                }

                viewModel.getCurrentPemasukan()?.let {
                    HomeFragment.pemasukan_user = it
                    pemasukan.text = Utilities.formatNumber(it)
                }

                viewModel.getCurrentPengeluaran()?.let {
                    HomeFragment.pengeluaran_user = it
                    pengeluaran.text = Utilities.formatNumber(it)
                }
            }
        }
    }

    inner class LastTransactionHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(viewModel: UserViewModel, fragment: Fragment, listener: Listener) {
            with(itemView) {
                rvhome.setHasFixedSize(true)
                rvhome.layoutManager = LinearLayoutManager(fragment.context)
                viewModel.getLastTransaction()?.let {
                    rvhome.adapter = TransactionsAdapter(it, object : TransactionsAdapter.Listener {
                        override fun onViewClick(transaction: TransactionEntity) {
                            listener.onTransactionViewClick(transaction)
                        }
                    })
                }
            }
        }
    }

    inner class StatistikHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            listLayout[position].equals("CARD", true) -> {
                CARD
            }
            listLayout[position].equals("LAST TRANSACTION",true) -> {
                LAST_TRANSACTION
            }
            else -> {
                STATISTIC
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CARD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_card, parent, false)
                CardHolder(view)
            }
            LAST_TRANSACTION -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_last_transaction, parent, false)
                LastTransactionHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_statistiic, parent, false)
                StatistikHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == CARD -> {
                (holder as LayoutAdapter.CardHolder).bind(viewModel, fragment, listener)
            }
            getItemViewType(position) == LAST_TRANSACTION -> {
                (holder as LayoutAdapter.LastTransactionHolder).bind(viewModel, fragment, listener)
            }
            getItemViewType(position) == STATISTIC -> {
                (holder as LayoutAdapter.StatistikHolder).bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return listLayout.size
    }
}