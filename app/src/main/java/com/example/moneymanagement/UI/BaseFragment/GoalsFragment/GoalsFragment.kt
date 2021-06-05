package com.example.moneymanagement.UI.BaseFragment.GoalsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.GoalsFragment.Adapter.GoalsAdapter
import com.example.moneymanagement.User.Goals.GoalsEntity
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.fragment_goals.*

class GoalsFragment : Fragment() {

    private lateinit var viewmodel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewmodel = ViewModelProvider(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.setHasFixedSize(true)
        recyclerview.layoutManager = LinearLayoutManager(context)
        viewmodel.getUnReachedGoals()?.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                recyclerview.adapter = GoalsAdapter(it, object : GoalsAdapter.Listener {
                    override fun onViewClick(goalsEntity: GoalsEntity) {
                        onViewAction(goalsEntity)
                    }
                })
                info_impian_belum_ada.visibility = View.GONE
            } else {
                info_impian_belum_ada.visibility = View.VISIBLE
                recyclerview.visibility = View.GONE
            }

        })
    }

    private fun onViewAction(goalsEntity: GoalsEntity) {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance() = GoalsFragment()
    }
}