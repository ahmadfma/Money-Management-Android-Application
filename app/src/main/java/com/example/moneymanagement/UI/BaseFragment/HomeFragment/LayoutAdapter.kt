package com.example.moneymanagement.UI.BaseFragment.HomeFragment

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.item_home_card.view.*
import kotlinx.android.synthetic.main.item_home_last_transaction.view.*
import kotlinx.android.synthetic.main.item_home_statistiic.view.*

private const val CARD = 0
private const val LAST_TRANSACTION = 1
private const val STATISTIC_PEMASUKAN = 2
private const val STATISTIC_PENGELUARAN = 3

class LayoutAdapter(
        private var listLayout: List<String>,
        private val info: CardView,
        private val viewModel: UserViewModel,
        private val fragment: Fragment,
        private val listener: Listener
    ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isTransactionEmpty = true

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
                    if(it.isNotEmpty()) {
                        rvhome.adapter = TransactionsAdapter(it, object : TransactionsAdapter.Listener {
                            override fun onViewClick(transaction: TransactionEntity) {
                                listener.onTransactionViewClick(transaction)
                            }
                        })
                        info.visibility = View.GONE
                        isTransactionEmpty = false
                    }
                }
            }
        }
    }

    inner class StatistikPemasukanHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val TAG = "StatistikPemasukanHolder"
        private var total_pemasukan: Long? = 0
        private var listTotalAmountOfPemasukanByCategory = arrayListOf<Long?>()
        private var listPercentOfPemasukanByCategory = arrayListOf<Float>()
        private var listColor = arrayListOf<Int>()

        fun bind() {
            with(itemView) {
                getTotalAmountAllCategory()
                if(total_pemasukan!! > 0) {
                    setPemasukanBarChart(this)
                    ket_tv.visibility = View.GONE
                    ket_rv.visibility = View.GONE
                    rp.text = Utilities.formatNumber(total_pemasukan!!)
                } else {
                    line.visibility = View.GONE
                    textvw.visibility = View.GONE
                    barChart.visibility = View.GONE
                    total.visibility = View.GONE
                    ket_tv.visibility = View.GONE
                    ket_rv.visibility = View.GONE
                }
            }
        }

        private fun getTotalAmountAllCategory() {
            total_pemasukan = viewModel.getTotalAmount("pemasukan")

            Utilities.listKateogri().forEach {
                val total = viewModel.getTotalAmountByCategory(it, "pemasukan")
                listTotalAmountOfPemasukanByCategory.add(total)
            }

            listTotalAmountOfPemasukanByCategory.forEach {
                if(it != null) {
                    val saldo = it.toFloat() / total_pemasukan!!.toFloat() * 100
                    listPercentOfPemasukanByCategory.add(saldo)
                } else {
                    listPercentOfPemasukanByCategory.add(0F)
                }
            }

            Log.d(TAG, "pemasukan : $total_pemasukan")
            Log.d(TAG, "list Total Amount Of Pemasukan By Category : $listTotalAmountOfPemasukanByCategory")
            Log.d(TAG, "list Percent Of Pemasukan By Category : $listPercentOfPemasukanByCategory")
        }

        private fun setPemasukanBarChart(view: View) {
            with(view) {
                val listEntry: ArrayList<BarEntry> = arrayListOf()
                var num = 1f
                Utilities.listKateogri().forEachIndexed { index, s ->
                    if(listTotalAmountOfPemasukanByCategory[index] != 0.toLong()) {
                        listEntry.add(BarEntry(num++, listTotalAmountOfPemasukanByCategory[index]!!.toFloat()))
                        listColor.add(ContextCompat.getColor(fragment.requireContext(), Utilities.listKateogriColor()[index]))
                    }
                }

                val dataset = BarDataSet(listEntry, "Pemasukan")
                dataset.colors = listColor
                dataset.valueTextColor = Color.BLACK
                dataset.valueTextSize = 12F

                val data = BarData(dataset)

                barChart.setFitBars(true)
                barChart.data = data
                barChart.description.text = ""
                barChart.description.textSize = 15F
                barChart.animateY(2000)
                barChart.legend.isEnabled = false
            }
        }
    }

    inner class StatistikPengeluaranHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val TAG = "StatistikPengeluaranHolder"
        private var total_pengeluaran: Long? = 0
        private var listTotalAmountOfPengeluaranByCategory = arrayListOf<Long?>()
        private var listPercentOfPengeluaranByCategory = arrayListOf<Float>()
        private var listColor = arrayListOf<Int>()

        fun bind() {
            with(itemView) {
                getTotalAmountAllCategory()
                if(total_pengeluaran!! > 0) {
                    setPengeluaranBarChart(this)
                    txt.text = "Total Pengeluaran : Rp."
                    rp.text = Utilities.formatNumber(total_pengeluaran!!)
                    ket_rv.setHasFixedSize(true)
                    ket_rv.layoutManager = LinearLayoutManager(fragment.requireContext())
                    ket_rv.adapter = KeteranganAdapter(Utilities.listKateogri(), Utilities.listKateogriColor())
                    textvw.text = "Statistik Pengeluaran"
                    line.visibility = View.GONE
                } else {
                    line.visibility = View.GONE
                    textvw.visibility = View.GONE
                    barChart.visibility = View.GONE
                    total.visibility = View.GONE
                    ket_tv.visibility = View.GONE
                    ket_rv.visibility = View.GONE
                }

            }
        }

        private fun getTotalAmountAllCategory() {
            total_pengeluaran = viewModel.getTotalAmount("pengeluaran")

            Utilities.listKateogri().forEach {
                val total = viewModel.getTotalAmountByCategory(it, "pengeluaran")
                listTotalAmountOfPengeluaranByCategory.add(total)
            }


            listTotalAmountOfPengeluaranByCategory.forEach {
                if(it != null) {
                    val saldo = it.toFloat() / total_pengeluaran!!.toFloat() * 100
                    listPercentOfPengeluaranByCategory.add(saldo)
                } else {
                    listPercentOfPengeluaranByCategory.add(0F)
                }
            }

            Log.d(TAG, "pengeluaran : $total_pengeluaran")
            Log.d(TAG, "list Total Amount Of pengeluaran By Category : $listTotalAmountOfPengeluaranByCategory")
            Log.d(TAG, "list Percent Of pengeluaran By Category : $listPercentOfPengeluaranByCategory")
        }

        private fun setPengeluaranBarChart(view: View) {
            with(view) {
                val listEntry: ArrayList<BarEntry> = arrayListOf()
                var num = 1f
                Utilities.listKateogri().forEachIndexed { index, s ->
                    if (listTotalAmountOfPengeluaranByCategory[index] != 0.toLong()) {
                        listEntry.add(
                            BarEntry(
                                num++,
                                listTotalAmountOfPengeluaranByCategory[index]!!.toFloat()
                            )
                        )
                        listColor.add(
                            ContextCompat.getColor(
                                fragment.requireContext(),
                                Utilities.listKateogriColor()[index]
                            )
                        )
                    }
                }

                val dataset = BarDataSet(listEntry, "Pengeluaran")
                dataset.colors = listColor
                dataset.valueTextColor = Color.BLACK
                dataset.valueTextSize = 12F

                val data = BarData(dataset)

                barChart.setFitBars(true)
                barChart.data = data
                barChart.description.text = ""
                barChart.description.textSize = 15F
                barChart.animateY(2000)
                barChart.legend.isEnabled = false
            }
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
            listLayout[position].equals("STATISTIC PEMASUKAN", true) -> {
                STATISTIC_PEMASUKAN
            }
            else -> {
                STATISTIC_PENGELUARAN
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
            STATISTIC_PEMASUKAN -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_statistiic, parent, false)
                StatistikPemasukanHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_statistiic, parent, false)
                StatistikPengeluaranHolder(view)
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
            getItemViewType(position) == STATISTIC_PEMASUKAN -> {
                (holder as LayoutAdapter.StatistikPemasukanHolder).bind()
            }
            getItemViewType(position) == STATISTIC_PENGELUARAN -> {
                (holder as LayoutAdapter.StatistikPengeluaranHolder).bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return listLayout.size
    }
}