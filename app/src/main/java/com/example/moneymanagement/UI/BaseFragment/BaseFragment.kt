package com.example.moneymanagement.UI.BaseFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.etebarian.meowbottomnavigation.MeowBottomNavigation.*
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.AddGoalsFragment.AddGoalsFragment
import com.example.moneymanagement.UI.AddTransactionFragment.AddTransactionFragment
import com.example.moneymanagement.UI.BaseFragment.GoalsFragment.GoalsFragment
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.HistoryFragment
import com.example.moneymanagement.UI.BaseFragment.HomeFragment.HomeFragment
import com.example.moneymanagement.UI.BaseFragment.NewsFragment.NotificationFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*
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

        bottom_navigation.setOnNavigationItemSelectedListener(navListener())
        bottom_navigation.selectedItemId = viewModel.selectedBottomNavigationID
        loadFragment()

        addBtn.setOnClickListener {
            showBottomSheet()
        }

        Log.d("BaseFragment", "onViewCreated")
    }

    private fun showBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
        dialogView.addTransaksi.setOnClickListener {
            bottomSheetDialog.dismiss()
            AddTransactionFragment.action = "add"
            findNavController().navigate(R.id.action_baseFragment_to_addTransactionFragment)
        }
        dialogView.addImpian.setOnClickListener {
            AddGoalsFragment.action = "add"
            bottomSheetDialog.dismiss()
            findNavController().navigate(R.id.action_baseFragment_to_addGoalsFragment)
        }
    }

    private fun loadFragment() {
        when(viewModel.selectedBottomNavigationID) {
            R.id.nav_home -> {
                loadHomeFragment()
            }
            R.id.nav_history -> {
                loadHistoryFragment()
            }
            R.id.nav_goals -> {
                loadGoalsFragment()
            }
            R.id.nav_notif -> {
                loadNotifFragment()
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
            R.id.nav_goals -> {
                loadGoalsFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_notif -> {
                loadNotifFragment()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadHomeFragment() {
        viewModel.selectedBottomNavigationID = R.id.nav_home
        lifecycleScope.launch(Dispatchers.IO) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, HomeFragment.newInstance())
                ?.commit()
        }
    }

    private fun loadHistoryFragment() {
        viewModel.selectedBottomNavigationID = R.id.nav_history
        lifecycleScope.launch(Dispatchers.IO) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, HistoryFragment.newInstance())
                ?.commit()
        }
    }

    private fun loadGoalsFragment() {
        viewModel.selectedBottomNavigationID = R.id.nav_goals
        lifecycleScope.launch {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, GoalsFragment.newInstance())
                ?.commit()
        }
    }

    private fun loadNotifFragment() {
        viewModel.selectedBottomNavigationID = R.id.nav_notif
        lifecycleScope.launch {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.childFragment, NotificationFragment.newInstance())
                ?.commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BaseFragment()
    }
}