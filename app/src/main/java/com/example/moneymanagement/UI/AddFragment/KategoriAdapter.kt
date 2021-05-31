package com.example.moneymanagement.UI.AddFragment

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import kotlinx.android.synthetic.main.item_kategori.view.*

class KategoriAdapter(private val listKategori: ArrayList<String>, private val listColor: ArrayList<Int>): RecyclerView.Adapter<KategoriAdapter.Holder>() {

    private var selected_radio_btn: Int? = null

    inner class Holder(itemview: View): RecyclerView.ViewHolder(itemview) {
        fun bind(kategori: String, color: Int, position: Int) {
            with(itemView) {
                line_color.setImageResource(color)
                text.text = kategori
                if(position == listKategori.size-1) {
                    underline.visibility = View.GONE
                }
                radio_btn.setOnCheckedChangeListener { buttonView, isChecked ->
                    Log.d("KategoriAdapter", "isChecked : $isChecked")
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


}