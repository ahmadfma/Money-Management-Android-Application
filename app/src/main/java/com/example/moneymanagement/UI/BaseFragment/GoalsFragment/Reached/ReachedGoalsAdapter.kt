package com.example.moneymanagement.UI.BaseFragment.GoalsFragment.Reached

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import com.example.moneymanagement.User.Goals.GoalsEntity
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.item_goals_reached.view.*

class ReachedGoalsAdapter(private val list: List<GoalsEntity>, val userViewModel: UserViewModel): RecyclerView.Adapter<ReachedGoalsAdapter.Holder>() {

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(goalsEntity: GoalsEntity, userViewModel: UserViewModel) {
            with(itemView) {
                setIcon(goalsEntity.category, iconr, null)
                catatanr.text = goalsEntity.note
                categoryr.text = goalsEntity.category
                targetr.text = Utilities.formatNumber(goalsEntity.amount)
                delete.setOnClickListener {
                    deleteGoals(goalsEntity, userViewModel)
                }
            }
        }

        private fun deleteGoals(goalsEntity: GoalsEntity, userViewModel: UserViewModel) {
            userViewModel.deleteGoals(goalsEntity)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReachedGoalsAdapter.Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goals_reached, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: ReachedGoalsAdapter.Holder, position: Int) {
        holder.bind(list[position], userViewModel)
    }

    override fun getItemCount(): Int {
        return list.size
    }


}