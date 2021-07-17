package com.example.moneymanagement.UI.BaseFragment.GoalsFragment.Reached

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.item_goals_reached_fragment.*

class ReachedFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.item_goals_reached_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcview_reached.setHasFixedSize(true)
        rcview_reached.layoutManager = LinearLayoutManager(requireContext())
        userViewModel.getReachedGoals()?.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                rcview_reached.adapter = ReachedGoalsAdapter(it, userViewModel)
                info_reached_isempty.visibility = View.GONE
            } else {
                info_reached_isempty.visibility = View.VISIBLE
                rcview_reached.visibility = View.GONE
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ReachedFragment()
    }
}