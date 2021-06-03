package com.example.moneymanagement.UI.BaseFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.etebarian.meowbottomnavigation.MeowBottomNavigation.*
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.AddFragment.AddFragment
import com.example.moneymanagement.UI.GoalsFragment.GoalsFragment
import com.example.moneymanagement.UI.HistoryFragment.HistoryFragment
import com.example.moneymanagement.UI.HomeFragment.HomeFragment
import com.example.moneymanagement.UI.NewsFragment.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BaseFragment : Fragment() {

    private lateinit var viewModel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottom_navigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_home))
        bottom_navigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_history))
        bottom_navigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_add))
        bottom_navigation.add(MeowBottomNavigation.Model(4, R.drawable.ic_target))
        bottom_navigation.add(MeowBottomNavigation.Model(5, R.drawable.ic_news))

        bottom_navigation.setOnClickMenuListener(ClickListener {
            when(it.id) {
                1 -> {
                    loadHomeFragment()
                }
                2 -> {
                    loadHistoryFragment()
                }
                3 -> {
                    loadAddFragment()
                }
                4 -> {
                    loadGoalsFragment()
                }
                5 -> {
                    loadNewsFragment()
                }
            }
            Log.d("BaseFragment", "setOnClickMenuListener")
        })

        bottom_navigation.setOnShowListener(ShowListener {
            when(it.id) {
                1 -> {
                    loadHomeFragment()
                }
            }
            Log.d("BaseFragment", "setOnShowListener")
        })

        bottom_navigation.setOnReselectListener(ReselectListener {
            // your codes
        })
        bottom_navigation.show(viewModel.selectedBottomNavigationID, false)
        Log.d("BaseFragment", "onViewCreated")
    }

    private fun loadHomeFragment() {
        viewModel.selectedBottomNavigationID = 1
        lifecycleScope.launch(Dispatchers.IO) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, HomeFragment.newInstance())
                ?.commit()
        }
    }

    private fun loadHistoryFragment() {
        viewModel.selectedBottomNavigationID = 2
        lifecycleScope.launch(Dispatchers.IO) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, HistoryFragment.newInstance())
                ?.commit()
        }
    }

    private fun loadAddFragment() {
        viewModel.selectedBottomNavigationID = 3
        lifecycleScope.launch {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, AddFragment.newInstance())
                ?.commit()
        }
    }

    private fun loadGoalsFragment() {
        viewModel.selectedBottomNavigationID = 4
        lifecycleScope.launch {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, GoalsFragment.newInstance())
                ?.commit()
        }
    }

    private fun loadNewsFragment() {
        viewModel.selectedBottomNavigationID = 5
        lifecycleScope.launch {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, NewsFragment.newInstance())
                ?.commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BaseFragment()
    }
}