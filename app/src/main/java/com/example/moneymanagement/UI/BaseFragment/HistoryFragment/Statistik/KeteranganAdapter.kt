package com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Statistik

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.item_statistik_item_keterangan.view.*

class KeteranganAdapter(val categories: ArrayList<String>, val colors: ArrayList<Int>, val amounts: ArrayList<Long?>, val type: Int): RecyclerView.Adapter<KeteranganAdapter.Holder>() {

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(category: String, color: Int, amount: Long) {
            with(itemView) {
                relativeLayout.background.setTint(relativeLayout.resources.getColor(color));
                kategori.text = category
                if(type == 1) {
                    rp.setTextColor(resources.getColor(R.color.hijau_tulisan))
                    total.setTextColor(resources.getColor(R.color.hijau_tulisan))
                } else {
                    rp.setTextColor(resources.getColor(R.color.merah_tulisan))
                    total.setTextColor(resources.getColor(R.color.merah_tulisan))
                }
                total.text = Utilities.formatNumber(amount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistik_item_keterangan, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(categories[position], colors[position], amounts[position]!!)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

}