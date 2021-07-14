package com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Statistik

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.HistoryViewModel
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Riwayat.TombolTanggalAdapter
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.item_history_fragment_statistik.*

const val TAG = "StatistikFragment"
class StatistikFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        return inflater.inflate(R.layout.item_history_fragment_statistik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMonth()
    }

    private fun loadMonth(){
        bulan_btn.setHasFixedSize(true)
        bulan_btn.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        historyViewModel.getAllTransactionsDate(userViewModel, this, object : HistoryViewModel.UI {
            override fun setTanggalUI(date: List<String>) {
                if(date.isNotEmpty()) {
                    setMonthBtn(date)
                    info_statistik_belum_ada.visibility = View.GONE
                } else {
                    info_statistik_belum_ada.visibility = View.VISIBLE
                    txt1st.visibility = View.GONE
                    txt2st.visibility = View.GONE
                }
            }
        })
    }

    private fun setMonthBtn(date: List<String>) {
        val month: MutableList<String> = date.map {
            val temp = it.split(" ").toTypedArray()
            temp[2]
        } as MutableList<String>

        val set = month.toSet().toMutableList()
        Log.d(TAG, "$set")
        bulan_btn.adapter = TombolBulanAdapter(set, object : TombolBulanAdapter.Listener {
            override fun onDateClick(date: String) {
                loadTransactionBasedOnDate(date)
            }
        })
    }

    private fun loadTransactionBasedOnDate(date: String) {

    }


    companion object {
        @JvmStatic
        fun newInstance() = StatistikFragment()
    }
}