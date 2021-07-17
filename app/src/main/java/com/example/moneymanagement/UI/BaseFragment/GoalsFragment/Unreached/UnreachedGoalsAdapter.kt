package com.example.moneymanagement.UI.BaseFragment.GoalsFragment.Unreached

import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.HomeFragment.HomeFragment
import com.example.moneymanagement.User.Goals.GoalsEntity
import com.example.moneymanagement.User.Saldo.SaldoEntity
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.item_goals_unreached.view.*
import java.util.*

class UnreachedGoalsAdapter(private val list: List<GoalsEntity>, private val userViewModel: UserViewModel, private val listener: Listener): RecyclerView.Adapter<UnreachedGoalsAdapter.Holder>() {

    interface Listener {
        fun onViewClick(goalsEntity: GoalsEntity)
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(goalsEntity: GoalsEntity, userViewModel: UserViewModel, listener: Listener) {
            with(itemView) {
                setIcon(goalsEntity.category, icon, null)
                category.text = goalsEntity.category
                catatan.text = goalsEntity.note

                //cek saldo user
                if(HomeFragment.saldo_user >= goalsEntity.amount) {
                    target2.text = Utilities.formatNumber(goalsEntity.amount)
                    progres.visibility = View.GONE
                    finish.visibility = View.VISIBLE
                    finish.setOnClickListener {
                        updateGoalsToReached(goalsEntity)
                    }
                } else {
                    target.text = Utilities.formatNumber(goalsEntity.amount)
                    saldo.text = Utilities.formatNumber(HomeFragment.saldo_user)
                    val progress = calculateProgres(goalsEntity.amount, HomeFragment.saldo_user)
                    progress_horizontal.progress = progress
                    progress_horizontal.isActivated = false
                    percent.text = "$progress% dari"
                    progres.visibility = View.VISIBLE
                    finish.visibility = View.GONE
                }

                itemView.setOnClickListener {
                    listener.onViewClick(goalsEntity)
                }

                Log.d("TESSTAMGGAL", Utilities.getDate())
            }
        }

        private fun updateGoalsToReached(goalsEntity: GoalsEntity) {
            goalsEntity.reached = true
            userViewModel.updateGoals(goalsEntity)
            userViewModel.insertTransaction(
                TransactionEntity(getID(), "pengeluaran", goalsEntity.category, goalsEntity.amount, "Mewujudkan Impian : ${goalsEntity.note}", Utilities.getDate())
            )
            userViewModel.insertUserSaldo(
                SaldoEntity(0, HomeFragment.saldo_user-goalsEntity.amount, HomeFragment.pemasukan_user, HomeFragment.pengeluaran_user+goalsEntity.amount)
            )
        }

        private fun getID(): String {
            val date = Calendar.getInstance().time.toString()
            val temp = date.split(" ")
            val jam = temp[3].split(":")

            val date2 = Calendar.getInstance().time
            val temp2 = DateFormat.format("yyyy.MM.dd", date2) as String
            val tanggal = temp2.split(".")

            Log.d("TESSTAMGGAL", "${tanggal[1]}${tanggal[2]}${tanggal[0]}${jam[0]}${jam[1]}${jam[2]}")

            return "${tanggal[1]}${tanggal[2]}${tanggal[0]}${jam[0]}${jam[1]}${jam[2]}"
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_goals_unreached, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], userViewModel, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }


}