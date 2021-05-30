package com.example.moneymanagement.UI.BaseFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.AddFragment.AddFragment
import com.example.moneymanagement.UI.GoalsFragment.GoalsFragment
import com.example.moneymanagement.UI.HistoryFragment.HistoryFragment
import com.example.moneymanagement.UI.HomeFragment.HomeFragment
import com.example.moneymanagement.UI.NewsFragment.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_base.*

class BaseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(savedInstanceState == null) {
            bottom_navigation.setOnNavigationItemSelectedListener(navListener())
            bottom_navigation.selectedItemId = R.id.nav_home
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, HomeFragment.newInstance())
                ?.commit()
        }

    }

    private fun navListener(): BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId) {
            R.id.nav_home -> {
                loadHomeFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_history -> {
                loadHistoryFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_add -> {
                loadAddFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_goals -> {
                loadGoalsFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_news -> {
                loadNewsFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadHomeFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, HomeFragment.newInstance())
            ?.commit()
    }

    private fun loadHistoryFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, HistoryFragment.newInstance())
            ?.commit()
    }

    private fun loadAddFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, AddFragment.newInstance())
            ?.commit()
    }

    private fun loadGoalsFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, GoalsFragment.newInstance())
            ?.commit()
    }

    private fun loadNewsFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, NewsFragment.newInstance())
            ?.commit()
    }



    companion object {
        @JvmStatic
        fun newInstance() = BaseFragment()
    }
}