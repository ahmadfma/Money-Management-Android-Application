package com.example.moneymanagement.UI.BaseFragment.GoalsFragment.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.HomeFragment.HomeFragment
import com.example.moneymanagement.User.Goals.GoalsEntity
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.item_goals.view.*

class GoalsAdapter(private val list: List<GoalsEntity>, private val listener: GoalsAdapter.Listener): RecyclerView.Adapter<GoalsAdapter.Holder>() {

    interface Listener {
        fun onViewClick(goalsEntity: GoalsEntity)
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(goalsEntity: GoalsEntity, listener: Listener) {
            with(itemView) {
                setIcon(goalsEntity.category, icon, null)
                category.text = goalsEntity.category
                catatan.text = goalsEntity.note
                target.text = Utilities.formatNumber(goalsEntity.amount)
                saldo.text = Utilities.formatNumber(HomeFragment.saldo_user)
            }
        }
        private fun setIcon(categori: String, icon: ImageView, line: ImageView?) {
            when(categori) {
                "Makanan & Minuman" -> {
                    icon.setImageResource(R.drawable.ic_fast_food)
                    line?.setImageResource(R.color.merah)
                }
                "Kecantikan & Kesehatan" -> {
                    icon.setImageResource(R.drawable.ic_healthy)
                    line?.setImageResource(R.color.pink)
                }
                "Sosial & Gaya Hidup" -> {
                    icon.setImageResource(R.drawable.ic_makeup)
                    line?.setImageResource(R.color.ungu)
                }
                "Entertainment" -> {
                    icon.setImageResource(R.drawable.ic_drum_set)
                    line?.setImageResource(R.color.biru)
                }
                "Transportasi" -> {
                    icon.setImageResource(R.drawable.ic_transportation)
                    line?.setImageResource(R.color.kuning)
                }
                "Lainnya" -> {
                    icon.setImageResource(R.drawable.ic_other)
                    line?.setImageResource(R.color.hijau)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goals, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }


}