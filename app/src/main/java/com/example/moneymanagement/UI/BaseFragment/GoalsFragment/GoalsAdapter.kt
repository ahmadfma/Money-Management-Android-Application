package com.example.moneymanagement.UI.BaseFragment.GoalsFragment

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

class GoalsAdapter(private val list: List<GoalsEntity>, private val listener: Listener): RecyclerView.Adapter<GoalsAdapter.Holder>() {

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
                progress_horizontal.progress = calculateProgres(goalsEntity.amount, HomeFragment.saldo_user)
                progress_horizontal.isActivated = false
                itemView.setOnClickListener {
                    listener.onViewClick(goalsEntity)
                }
            }
        }

        private fun setIcon(categori: String, icon: ImageView, line: ImageView?) {
            Utilities.listKateogri().forEachIndexed { index, s ->
                if(categori == s) {
                    icon.setImageResource(Utilities.listKategoriIcon("GOAL")[index])
                    line?.setImageResource(Utilities.listKateogriColor()[index])
                }
            }
        }
    }

    private fun calculateProgres(amount: Long, saldoUser: Long): Int {
        val cek: Double = (saldoUser.toDouble() / amount.toDouble()) * 100
        return if(cek < 1) {
            1
        } else {
            if(cek > 100) {
                100
            } else {
                cek.toInt()
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