package com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Statistik

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.HistoryFragment.HistoryViewModel
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.item_history_fragment_riwayat.*
import kotlinx.android.synthetic.main.item_history_fragment_statistik.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "StatistikFragment"
class StatistikFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var historyViewModel: HistoryViewModel

    private val listLayout = arrayListOf<String>(
        "CHART",
        "PEMASUKAN",
        "PENGELUARAN"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        return inflater.inflate(R.layout.item_history_fragment_statistik, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMonth()
        expandBtn()
    }

    private fun expandBtn() {
        expandS.setOnClickListener {
            if(expandS.tag == "up") {
                bulan_btn.visibility = View.GONE
                expandS.tag = "down"
                expandS.setImageResource(R.drawable.ic_expand_down)
            } else if(expandS.tag == "down") {
                bulan_btn.visibility = View.VISIBLE
                expandS.tag = "up"
                expandS.setImageResource(R.drawable.ic_expand_up)
            }
        }
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
        loadStatisticBasedOnMonth(set[0])
    }

    private fun loadStatisticBasedOnMonth(month: String) {
        rcview_layout.setHasFixedSize(true)
        rcview_layout.layoutManager = LinearLayoutManager(requireContext())
        rcview_layout.adapter = StatistikLayoutAdapter(listLayout, this, userViewModel, month)
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatistikFragment()
    }
}