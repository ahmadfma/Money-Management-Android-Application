package com.example.moneymanagement.UI.BaseFragment.GoalsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.GoalsFragment.Reached.ReachedFragment
import com.example.moneymanagement.UI.BaseFragment.GoalsFragment.Unreached.UnreachedFragment
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Riwayat.RiwayatFragment
import com.example.moneymanagement.User.Goals.GoalsEntity
import kotlinx.android.synthetic.main.fragment_goals.*

class GoalsFragment : Fragment() {

    var selectedBtn = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUnReachedFragment()

        reachedBtn.setOnClickListener {
            if(selectedBtn != 1) {
                loadReachedFragment()
                selectedBtn = 1
            }
        }
        unreachedBtn.setOnClickListener {
            if(selectedBtn != 2) {
                loadUnReachedFragment()
                selectedBtn = 2
            }
        }

    }

    private fun loadUnReachedFragment() {
        selectedBtn(unreachedBtn, unreachedtxt)
        unSelectedBtn(reachedBtn, reachedtxt)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container_fragment_goals, UnreachedFragment())
            ?.commit()
    }

    private fun loadReachedFragment() {
        selectedBtn(reachedBtn, reachedtxt)
        unSelectedBtn(unreachedBtn, unreachedtxt)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container_fragment_goals, ReachedFragment())
            ?.commit()
    }

    private fun selectedBtn(cv: CardView, txt: TextView) {
        cv.setCardBackgroundColor(resources.getColor(R.color.primary))
        txt.setTextColor(resources.getColor(R.color.white))
    }

    private fun unSelectedBtn(cv: CardView, txt: TextView) {
        cv.setCardBackgroundColor(resources.getColor(R.color.white))
        txt.setTextColor(resources.getColor(R.color.black))
    }

    companion object {
        @JvmStatic
        fun newInstance() = GoalsFragment()
        var selectedGoals: GoalsEntity? = null
    }
}