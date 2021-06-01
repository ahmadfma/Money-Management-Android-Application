package com.example.moneymanagement.UI.HomeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvhome.setHasFixedSize(true)
        rvhome.layoutManager = LinearLayoutManager(context)
        viewModel.getLastTransaction()?.observe(viewLifecycleOwner, Observer {
            rvhome.adapter = TransactionsAdapter(it, object : TransactionsAdapter.Listener {
                override fun onViewClick(transaction: TransactionEntity) {
                    onViewAction(transaction)
                }
            })
        })
    }

    private fun onViewAction(transaction: TransactionEntity) {
        Toast.makeText(context, "${transaction.title}", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}