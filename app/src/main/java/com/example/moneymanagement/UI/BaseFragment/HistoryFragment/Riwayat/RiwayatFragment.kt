package com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Riwayat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.AddTransactionFragment.AddTransactionFragment
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.HistoryViewModel
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.TAG
import com.example.moneymanagement.UI.BaseFragment.HomeFragment.TransactionsAdapter
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.item_history_fragment_riwayat.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RiwayatFragment : Fragment() {

    private lateinit var viewmodel_user: UserViewModel
    private lateinit var viewmodel_fragment: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewmodel_user = ViewModelProvider(this).get(UserViewModel::class.java)
        viewmodel_fragment = ViewModelProvider(this).get(HistoryViewModel::class.java)
        return inflater.inflate(R.layout.item_history_fragment_riwayat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tanggal_btn.setHasFixedSize(true)
        tanggal_btn.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        expandBtn()
        viewmodel_fragment.getAllTransactionsDate(viewmodel_user, this, object: HistoryViewModel.UI {
            override fun setTanggalUI(date: List<String>) {
                if(date.isNotEmpty()) {
                    setTombolTanggal(date)
                    loadTransactionBasedOnDate(date[0].split(" ").toTypedArray().let {
                        "${it[1]} ${it[2]}"
                    })
                    info_history_belum_ada.visibility = View.GONE
                } else {
                    tx1.visibility = View.GONE
                    tx2.visibility = View.GONE
                    info_history_belum_ada.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun expandBtn() {
        expand.setOnClickListener {
            if(expand.tag == "up") {
                tanggal_btn.visibility = View.GONE
                expand.tag = "down"
                expand.setImageResource(R.drawable.ic_expand_down)
            } else if(expand.tag == "down") {
                tanggal_btn.visibility = View.VISIBLE
                expand.tag = "up"
                expand.setImageResource(R.drawable.ic_expand_up)
            }
        }
    }

    private fun setTombolTanggal(data: List<String>?) {
        val date: MutableList<String>? = data?.map {
            val temp = it.split(" ").toTypedArray()
            "${temp[1]} ${temp[2]}"
        } as MutableList<String>

        val set = date?.toSet()
        val list = set?.toMutableList()
        tanggal_btn.adapter = TombolTanggalAdapter(list, object : TombolTanggalAdapter.Listener {
            override fun onDateClick(date: String) {
                loadTransactionBasedOnDate(date)
                Log.d(TAG, date)
            }
        })
    }

    private fun loadTransactionBasedOnDate(date: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                val listTransaksi = async { viewmodel_fragment.getTransactionsBasedOnDate(viewmodel_user, date) }.await()
                daftar_riwayat.setHasFixedSize(true)
                daftar_riwayat.layoutManager = LinearLayoutManager(context)
                daftar_riwayat.adapter = listTransaksi?.let {
                    TransactionsAdapter(it, object : TransactionsAdapter.Listener {
                        override fun onViewClick(transaction: TransactionEntity) {
                            onViewAction(transaction)
                        }
                    })
                }
            }
        }
    }

    private fun onViewAction(transaction: TransactionEntity) {
        AddTransactionFragment.selected_transaction = transaction
        AddTransactionFragment.action = "update"
        findNavController().navigate(R.id.action_baseFragment_to_addTransactionFragment)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RiwayatFragment()
    }
}