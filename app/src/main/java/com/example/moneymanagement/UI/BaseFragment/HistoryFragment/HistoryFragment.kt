package com.example.moneymanagement.UI.BaseFragment.HistoryFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Riwayat.RiwayatFragment
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Statistik.StatistikFragment
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.*

const val TAG = "HistoryFragment"

class HistoryFragment : Fragment() {

    private var selectedBtn = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRiwayatFragment()
        riwayatbtn.setOnClickListener {
            if(selectedBtn != 1) {
                loadRiwayatFragment()
            }
        }
        statistikbtn.setOnClickListener {
            if(selectedBtn != 2) {
                loadStatistikFragment()
            }
        }
    }

    private fun loadRiwayatFragment() {
        selectedBtn(riwayatbtn, riwayattxt)
        unSelectedBtn(statistikbtn, statistiktxt)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container_fragment, RiwayatFragment())
            ?.commit()
        selectedBtn = 1
    }

    private fun loadStatistikFragment() {
        unSelectedBtn(riwayatbtn, riwayattxt)
        selectedBtn(statistikbtn, statistiktxt)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container_fragment, StatistikFragment())
            ?.commit()
        selectedBtn = 2
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
        fun newInstance() = HistoryFragment()
    }
}