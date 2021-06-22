package com.example.moneymanagement.UI.BaseFragment.HomeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import kotlinx.android.synthetic.main.item_home_statistic_keterangan.view.*

class KeteranganAdapter(private val listCategory: ArrayList<String>, private val listColor: ArrayList<Int>): RecyclerView.Adapter<KeteranganAdapter.Holder>() {

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(category: String, color: Int) {
            with(itemView) {
                colorvw.setImageResource(color)
                txt.text = category
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeteranganAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_statistic_keterangan, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: KeteranganAdapter.Holder, position: Int) {
        holder.bind(listCategory[position], listColor[position])
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }


}