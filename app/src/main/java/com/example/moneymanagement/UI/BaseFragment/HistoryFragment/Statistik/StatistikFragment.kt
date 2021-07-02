package com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Statistik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moneymanagement.R

class StatistikFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.item_history_fragment_statistik, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatistikFragment()
    }
}