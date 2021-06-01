package com.example.moneymanagement.UI.HistoryFragment.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import kotlinx.android.synthetic.main.item_tombol_tanggal.view.*

class TombolTanggalAdapter(private val listTanggal: List<String>?): RecyclerView.Adapter<TombolTanggalAdapter.Holder>() {

    private var selectedIndex = 0

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(tanggal: String) {
            with(itemView) {
                val temp = tanggal.split(" ").toTypedArray()
                tanggal_tombol.text = temp[0]
                bulan_tombol.text = temp[1]
                if(selectedIndex == absoluteAdapterPosition) {
                    tanggal_tombol.setTextColor(resources.getColor(R.color.white))
                    bulan_tombol.setTextColor(resources.getColor(R.color.white))
                    img.setImageResource(R.color.primary)
                } else {
                    tanggal_tombol.setTextColor(resources.getColor(R.color.black))
                    bulan_tombol.setTextColor(resources.getColor(R.color.black))
                    img.setImageResource(R.color.white)
                }

                itemView.setOnClickListener {
                    tanggal_tombol.setTextColor(resources.getColor(R.color.white))
                    bulan_tombol.setTextColor(resources.getColor(R.color.white))
                    img.setImageResource(R.color.primary)
                    if(selectedIndex != absoluteAdapterPosition) {
                        selectedIndex = absoluteAdapterPosition
                        notifyDataSetChanged()
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tombol_tanggal, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(listTanggal!![position])
    }

    override fun getItemCount(): Int {
        return listTanggal!!.size
    }


}