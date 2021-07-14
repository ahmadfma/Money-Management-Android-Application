package com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Statistik

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.HistoryViewModel
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.item_history_fragment_statistik.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            override fun onDateClick(month: String) {
                loadStatisticBasedOnMonth(month)
            }
        })
    }

    private fun loadStatisticBasedOnMonth(month: String) = lifecycleScope.launch(Dispatchers.IO) {
        loadTotalPemasukan(month)
        loadTotalPengeluaran(month)
    }

    private suspend fun loadTotalPemasukan(month: String) {
        withContext(lifecycleScope.coroutineContext + Dispatchers.IO) {
            val pemasukan = userViewModel.getTotalAmountByMonth("%$month%", "pemasukan")
            Log.d(TAG, "total pemasukan bulan $month = $pemasukan")
            Utilities.listKateogri().forEach {
                val totalamount = userViewModel.getTotalAmountByMonthAndCategory("%$month%", "pemasukan", it)
                Log.d(TAG, "total pemasukan bulan $month = $totalamount, kategori $it")
            }
        }
    }

    private suspend fun loadTotalPengeluaran(month: String) {
        withContext(lifecycleScope.coroutineContext + Dispatchers.IO) {
            val pengeluaran = userViewModel.getTotalAmountByMonth("%$month%", "pengeluaran")
            Log.d(TAG, "total pengeluran bulan $month = $pengeluaran")
            Utilities.listKateogri().forEach {
                val totalamount = userViewModel.getTotalAmountByMonthAndCategory("%$month%", "pengeluaran", it)
                Log.d(TAG, "total pengeluaran bulan $month = $totalamount, kategori $it")
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatistikFragment()
    }
}