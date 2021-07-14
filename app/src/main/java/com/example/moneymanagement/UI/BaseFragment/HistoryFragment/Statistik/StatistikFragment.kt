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
import kotlinx.android.synthetic.main.item_history_fragment_statistik.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val TAG = "StatistikFragment"
class StatistikFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var historyViewModel: HistoryViewModel
    private var totalPemasukan: Long? = 0
    private var totalPengeluaran: Long? = 0
    private var listPengeluaranByCategory: ArrayList<Long?> = arrayListOf()
    private var listPemasukanByCategory: ArrayList<Long?> = arrayListOf()
    private val listPercentPemasukan = mutableListOf<Float>()
    private val listPercentPengeluaran = mutableListOf<Float>()

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
        loadStatisticBasedOnMonth(set[0])
    }

    private fun loadStatisticBasedOnMonth(month: String) = lifecycleScope.launch(Dispatchers.IO) {
        clearList()
        loadTotalPemasukan(month)
        loadTotalPengeluaran(month)
        loadStatistic(pieChartPemasukan, "Pemasukan", totalPemasukan, listPemasukanByCategory, listPercentPemasukan)
        loadStatistic(pieChartPengeluaran, "Pengeluaran", totalPengeluaran, listPengeluaranByCategory, listPercentPengeluaran)
    }

    private fun clearList() {
        listPemasukanByCategory.clear()
        listPengeluaranByCategory.clear()
        listPercentPemasukan.clear()
        listPercentPengeluaran.clear()
    }

    private suspend fun loadTotalPemasukan(month: String) {
        withContext(lifecycleScope.coroutineContext + Dispatchers.IO) {
            totalPemasukan = userViewModel.getTotalAmountByMonth("%$month%", "pemasukan")
            Log.d(TAG, "total pemasukan bulan $month = $totalPemasukan")
            Utilities.listKateogri().forEach {
                val totalamount = userViewModel.getTotalAmountByMonthAndCategory("%$month%", "pemasukan", it)
                Log.d(TAG, "total pemasukan bulan $month = $totalamount, kategori $it")
                listPemasukanByCategory.add(totalamount)
            }
        }
    }

    private suspend fun loadTotalPengeluaran(month: String) {
        withContext(lifecycleScope.coroutineContext + Dispatchers.IO) {
            totalPengeluaran = userViewModel.getTotalAmountByMonth("%$month%", "pengeluaran")
            Log.d(TAG, "total pengeluran bulan $month = $totalPengeluaran")
            Utilities.listKateogri().forEach {
                val totalamount = userViewModel.getTotalAmountByMonthAndCategory("%$month%", "pengeluaran", it)
                Log.d(TAG, "total pengeluaran bulan $month = $totalamount, kategori $it")
                listPengeluaranByCategory.add(totalamount)
            }
        }
    }

    private fun loadStatistic(pieChart: PieChart, type: String, total: Long?, listAmount: ArrayList<Long?>, listPercent: MutableList<Float>) {
        listPercentPemasukan.clear()
        listPercentPengeluaran.clear()

        listAmount.forEach {
            if(it != null && it != 0L) {
                val percent = it.toFloat() / total!!.toFloat() * 100
                listPercent.add(percent)
            } else {
                listPercent.add(0F)
            }
        }

        //load pie chart

        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setDrawEntryLabels(false)
        pieChart.centerText = type
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false

        val colors = arrayListOf<Int>()
        val entries = arrayListOf<PieEntry>()
        Utilities.listKateogri().forEachIndexed { index, s ->
            if(listPercent[index] != 0.0F) {
                entries.add(PieEntry(listPercent[index], s))
                colors.add(ContextCompat.getColor(requireContext(), Utilities.listKateogriColor()[index]))
            }
        }

        val dataset = PieDataSet(entries, type)
        dataset.colors = colors

        val data = PieData(dataset)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChartPemasukan))
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.BLACK)

        pieChart.data = data
        pieChart.invalidate()
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatistikFragment()
    }
}