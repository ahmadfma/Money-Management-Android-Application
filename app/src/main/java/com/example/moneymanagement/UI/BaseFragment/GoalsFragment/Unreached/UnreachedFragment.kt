package com.example.moneymanagement.UI.BaseFragment.GoalsFragment.Unreached

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.AddGoalsFragment.AddGoalsFragment
import com.example.moneymanagement.UI.BaseFragment.GoalsFragment.GoalsAdapter
import com.example.moneymanagement.UI.BaseFragment.GoalsFragment.GoalsFragment
import com.example.moneymanagement.User.Goals.GoalsEntity
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.item_goals_unreached_fragment.*

class UnreachedFragment : Fragment() {
    private lateinit var viewmodel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewmodel = ViewModelProvider(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.item_goals_unreached_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcview_unreached.setHasFixedSize(true)
        rcview_unreached.layoutManager = LinearLayoutManager(context)
        viewmodel.getUnReachedGoals()?.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                rcview_unreached.adapter = GoalsAdapter(it, object : GoalsAdapter.Listener {
                    override fun onViewClick(goalsEntity: GoalsEntity) {
                        onViewAction(goalsEntity)
                    }
                })
                info_unreached_isempty.visibility = View.GONE
            } else {
                info_unreached_isempty.visibility = View.VISIBLE
                rcview_unreached.visibility = View.GONE
            }

        })
    }

    private fun onViewAction(goalsEntity: GoalsEntity) {
        GoalsFragment.selectedGoals = goalsEntity
        AddGoalsFragment.action = "update"
        findNavController().navigate(R.id.action_baseFragment_to_addGoalsFragment)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = UnreachedFragment()
    }
}