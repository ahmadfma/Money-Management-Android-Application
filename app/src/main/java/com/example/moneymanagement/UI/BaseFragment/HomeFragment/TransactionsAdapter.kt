package com.example.moneymanagement.UI.BaseFragment.HomeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.item_transaksi.view.*

class TransactionsAdapter(private val listTransaction: List<TransactionEntity>, private val listener: Listener): RecyclerView.Adapter<TransactionsAdapter.Holder>() {

    interface Listener {
        fun onViewClick(transaction: TransactionEntity)
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(transaction: TransactionEntity, listener: Listener) {
            with(itemView) {
                setIcon(transaction.category, icon, line_color)
                jumlah_saldo.text = Utilities.formatNumber(transaction.amount.toLong())
                when(transaction.type) {
                    "pemasukan" -> {
                        rp.text = "+Rp"
                        rp.setTextColor(resources.getColor(R.color.hijau_tulisan))
                        jumlah_saldo.setTextColor(resources.getColor(R.color.hijau_tulisan))
                    }
                    "pengeluaran" -> {
                        rp.text = "-Rp"
                        rp.setTextColor(resources.getColor(R.color.merah_tulisan))
                        jumlah_saldo.setTextColor(resources.getColor(R.color.merah_tulisan))
                    }
                }
                tanggal.text = transaction.date
                judul.text = transaction.title
                itemView.setOnClickListener {
                    listener.onViewClick(transaction)
                }
            }
        }

        private fun setIcon(categori: String, icon: ImageView, line: ImageView) {
            Utilities.listKateogri().forEachIndexed { index, s ->
                if(categori == s) {
                    icon.setImageResource(Utilities.listKategoriIcon("TRANSACTION")[index])
                    line.setImageResource(Utilities.listKateogriColor()[index])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaksi, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: TransactionsAdapter.Holder, position: Int) {
        holder.bind(listTransaction[position], listener)
    }

    override fun getItemCount(): Int {
        return listTransaction.size
    }


}