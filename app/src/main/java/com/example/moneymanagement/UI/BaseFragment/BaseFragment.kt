package com.example.moneymanagement.UI.BaseFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.AddFragment.AddFragment
import com.example.moneymanagement.UI.GoalsFragment.GoalsFragment
import com.example.moneymanagement.UI.HistoryFragment.HistoryFragment
import com.example.moneymanagement.UI.HomeFragment.HomeFragment
import com.example.moneymanagement.UI.NewsFragment.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_base.*

class BaseFragment : Fragment() {

    private lateinit var viewModel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_navigation.setOnNavigationItemSelectedListener(navListener())
        bottom_navigation.selectedItemId = viewModel.selectedBottomNavigationID
        loadFragment()

    }

    private fun loadFragment() {
        when(viewModel.selectedBottomNavigationID) {
            R.id.nav_home -> {
                loadHomeFragment()
            }
            R.id.nav_history -> {
                loadHistoryFragment()
            }
            R.id.nav_add -> {
                loadAddFragment()
            }
            R.id.nav_goals -> {
                loadGoalsFragment()
            }
            R.id.nav_news -> {
                loadNewsFragment()
            }
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
        viewModel.selectedBottomNavigationID = R.id.nav_home
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, HomeFragment.newInstance())
            ?.commit()
    }

    private fun loadHistoryFragment() {
        viewModel.selectedBottomNavigationID = R.id.nav_history
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, HistoryFragment.newInstance())
            ?.commit()
    }

    private fun loadAddFragment() {
        viewModel.selectedBottomNavigationID = R.id.nav_add
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, AddFragment.newInstance())
            ?.commit()
    }

    private fun loadGoalsFragment() {
        viewModel.selectedBottomNavigationID = R.id.nav_goals
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, GoalsFragment.newInstance())
            ?.commit()
    }

    private fun loadNewsFragment() {
        viewModel.selectedBottomNavigationID = R.id.nav_news
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.childFragment, NewsFragment.newInstance())
            ?.commit()
    }



    companion object {
        @JvmStatic
        fun newInstance() = BaseFragment()
    }
}