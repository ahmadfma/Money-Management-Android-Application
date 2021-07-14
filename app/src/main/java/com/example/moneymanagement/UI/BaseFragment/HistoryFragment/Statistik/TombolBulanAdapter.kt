package com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Statistik

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import kotlinx.android.synthetic.main.item_tombol_bulan.view.*

class TombolBulanAdapter(private val listTanggal: List<String>?, private val listener: Listener): RecyclerView.Adapter<TombolBulanAdapter.Holder>() {

    interface Listener {
        fun onDateClick(date: String)
    }

    private var selectedIndex = 0

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(tanggal: String, listener: Listener) {
            with(itemView) {

                bulan_statistik.text = tanggal

                if(selectedIndex == absoluteAdapterPosition) {
                    bulan_statistik.setTextColor(resources.getColor(R.color.white))
                    img.setImageResource(R.color.primary)
                } else {
                    bulan_statistik.setTextColor(resources.getColor(R.color.black))
                    img.setImageResource(R.color.white)
                }

                itemView.setOnClickListener {
                    bulan_statistik.setTextColor(resources.getColor(R.color.white))
                    img.setImageResource(R.color.primary)
                    if(selectedIndex != absoluteAdapterPosition) {
                        selectedIndex = absoluteAdapterPosition
                        notifyDataSetChanged()
                    }
                    listener.onDateClick(tanggal)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tombol_bulan, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(listTanggal!![position], listener)
    }

    override fun getItemCount(): Int {
        return listTanggal!!.size
    }
}