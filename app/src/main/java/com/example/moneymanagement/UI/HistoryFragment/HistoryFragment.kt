package com.example.moneymanagement.UI.HistoryFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.HistoryFragment.Adapter.TombolTanggalAdapter
import com.example.moneymanagement.UI.HomeFragment.TransactionsAdapter
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.coroutines.*

const val TAG = "HistoryFragment"

class HistoryFragment : Fragment() {

    private lateinit var viewmodel_user: UserViewModel
    private lateinit var viewmodel_fragment: HistoryViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewmodel_user = ViewModelProvider(this).get(UserViewModel::class.java)
        viewmodel_fragment = ViewModelProvider(this).get(HistoryViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tanggal_btn.setHasFixedSize(true)
        tanggal_btn.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewmodel_fragment.getAllTransactionsDate(viewmodel_user, this, object: HistoryViewModel.UI {
            override fun setTanggalUI(date: List<String>) {
                setTombolTanggal(date)
                loadTransactionBasedOnDate(date[0])
            }
        })

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
            }

        })
    }

    private fun loadTransactionBasedOnDate(date: String) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                val listTransaksi = async { viewmodel_fragment.getTransactionsBasedOnDate(viewmodel_user, date) }.await()
                daftar_riwayat.setHasFixedSize(true)
                daftar_riwayat.layoutManager = LinearLayoutManager(context)
                daftar_riwayat.adapter = listTransaksi?.let { TransactionsAdapter(it) }
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}