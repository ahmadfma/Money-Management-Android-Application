package com.example.moneymanagement.UI.AddTransactionFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import kotlinx.android.synthetic.main.item_kategori.view.*

class KategoriAdapter(private val listKategori: ArrayList<String>, private val listColor: ArrayList<Int>): RecyclerView.Adapter<KategoriAdapter.Holder>() {

    inner class Holder(itemview: View): RecyclerView.ViewHolder(itemview) {
        fun bind(kategori: String, color: Int, position: Int) {
            with(itemView) {
                line_color.setImageResource(color)
                text.text = kategori
                checkCategory()
                if(selected_radio_btn == -1) {
                    radio_btn.isChecked = false
                } else {
                    radio_btn.isChecked = selected_radio_btn == position
                }

                itemView.setOnClickListener {
                    radio_btn.isChecked = true
                    KategoriAdapter.kategori = kategori
                    if(selected_radio_btn != position) {
                        notifyDataSetChanged()
                        selected_radio_btn = position
                    }
                }

            }
        }
        private fun checkCategory() {
            when(selected_radio_btn) {
                0 -> {
                    kategori = "Makanan & Minuman"
                }
                1 -> {
                    kategori = "Kecantikan & Kesehatan"
                }
                2 -> {
                    kategori = "Sosial & Gaya Hidup"
                }
                3 -> {
                    kategori = "Entertainment"
                }
                4 -> {
                    kategori = "Transportasi"
                }
                5 -> {
                    kategori = "Lainnya"
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kategori, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: KategoriAdapter.Holder, position: Int) {
        holder.bind(listKategori[position], listColor[position], position)
    }

    override fun getItemCount(): Int {
        return listKategori.size
    }


    companion object {
        var kategori = ""
        var selected_radio_btn = -1
    }

}