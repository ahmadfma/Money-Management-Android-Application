package com.example.moneymanagement.UI.BaseFragment.HistoryFragment.Statistik

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymanagement.R
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.synthetic.main.item_statistik_chart.view.*
import kotlinx.android.synthetic.main.item_statistik_pemasukan.view.*
import kotlinx.android.synthetic.main.item_statistik_pengeluaran.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val CHART = 0
private const val PEMASUKAN = 1
private const val PENGELUARAN = 2

class StatistikLayoutAdapter(val listLayout: ArrayList<String>, val frag: Fragment, val userViewModel: UserViewModel, val month: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var totalPemasukan: Long? = 0
    private var totalPengeluaran: Long? = 0
    private var listPengeluaranByCategory: ArrayList<Long?> = arrayListOf()
    private var listPemasukanByCategory: ArrayList<Long?> = arrayListOf()
    private val listPercentPemasukan = mutableListOf<Float>()
    private val listPercentPengeluaran = mutableListOf<Float>()

    inner class ChartHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
        fun bind(month: String) {
            with(itemView) {
                clearList()
                loadTotalPemasukan(month)
                loadTotalPengeluaran(month)
                frag.lifecycleScope.launch {
                    loadStatistic(pieChartPemasukan, "Pemasukan", totalPemasukan, listPemasukanByCategory, listPercentPemasukan)
                    loadStatistic(pieChartPengeluaran, "Pengeluaran", totalPengeluaran, listPengeluaranByCategory, listPercentPengeluaran)
                    pemasukan_statistik.text = Utilities.formatNumber(totalPemasukan!!)
                    pengeluaran_statistik.text = Utilities.formatNumber(totalPengeluaran!!)
                }
            }
        }
        private fun loadTotalPemasukan(month: String) {
            totalPemasukan = userViewModel.getTotalAmountByMonth("%$month%", "pemasukan")
            Log.d(TAG, "total pemasukan bulan $month = $totalPemasukan")
            Utilities.listKateogri().forEach {
                val totalamount = userViewModel.getTotalAmountByMonthAndCategory("%$month%", "pemasukan", it)
                Log.d(TAG, "total pemasukan bulan $month = $totalamount, kategori $it")
                listPemasukanByCategory.add(totalamount)
            }
        }

        private fun loadTotalPengeluaran(month: String) {
            totalPengeluaran = userViewModel.getTotalAmountByMonth("%$month%", "pengeluaran")
            Log.d(TAG, "total pengeluran bulan $month = $totalPengeluaran")
            Utilities.listKateogri().forEach {
                val totalamount = userViewModel.getTotalAmountByMonthAndCategory("%$month%", "pengeluaran", it)
                Log.d(TAG, "total pengeluaran bulan $month = $totalamount, kategori $it")
                listPengeluaranByCategory.add(totalamount)
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
                    colors.add(ContextCompat.getColor(frag.requireContext(), Utilities.listKateogriColor()[index]))
                }
            }

            val dataset = PieDataSet(entries, type)
            dataset.colors = colors
            dataset.sliceSpace = 3f

            val data = PieData(dataset)
            data.setDrawValues(true)
            data.setValueFormatter(PercentFormatter(pieChart))
            data.setValueTextSize(10f)
            data.setValueTextColor(Color.BLACK)

            pieChart.data = data
            pieChart.invalidate()
            pieChart.animateY(1500, Easing.EaseInOutQuad)
        }

        private fun clearList() {
            listPemasukanByCategory.clear()
            listPengeluaranByCategory.clear()
            listPercentPemasukan.clear()
            listPercentPengeluaran.clear()
        }
    }

    inner class PemasukanHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {
                rcview_pemasukan.setHasFixedSize(true)
                rcview_pemasukan.layoutManager = LinearLayoutManager(frag.requireContext())
                val listKategori = arrayListOf<String>()
                val listColor = arrayListOf<Int>()
                val amount = arrayListOf<Long?>()
                listPemasukanByCategory.forEachIndexed { index, l ->
                    if(l != 0L) {
                        amount.add(l)
                        listKategori.add(Utilities.listKateogri()[index])
                        listColor.add(Utilities.listKateogriColor()[index])
                    }
                }
                if(amount.size != 0) {
                    rcview_pemasukan.adapter = KeteranganAdapter(listKategori, listColor, amount, 1)
                } else {
                    txtPem.visibility = View.GONE
                    rcview_pemasukan.visibility = View.GONE
                    linePem.visibility = View.GONE
                }
            }
        }
    }

    inner class PengeluaranHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {
                rcview_pengeluaran.setHasFixedSize(true)
                rcview_pengeluaran.layoutManager = LinearLayoutManager(frag.requireContext())
                val listKategori = arrayListOf<String>()
                val listColor = arrayListOf<Int>()
                val amount = arrayListOf<Long?>()
                listPengeluaranByCategory.forEachIndexed { index, l ->
                    if(l != 0L) {
                        amount.add(l)
                        listKategori.add(Utilities.listKateogri()[index])
                        listColor.add(Utilities.listKateogriColor()[index])
                    }
                }

                if(amount.size != 0) {
                    rcview_pengeluaran.adapter = KeteranganAdapter(listKategori, listColor, amount, 2)
                } else {
                    txtPeng.visibility = View.GONE
                    rcview_pengeluaran.visibility = View.GONE
                }

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            listLayout[position].equals("CHART", true) -> {
                CHART
            }
            listLayout[position].equals("PEMASUKAN",true) -> {
                PEMASUKAN
            }
            else -> {
                PENGELUARAN
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CHART -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistik_chart, parent, false)
                ChartHolder(view)
            }
            PEMASUKAN -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistik_pemasukan, parent, false)
                PemasukanHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_statistik_pengeluaran, parent, false)
                PengeluaranHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            getItemViewType(position) == CHART -> {
                (holder as StatistikLayoutAdapter.ChartHolder).bind(month)
            }
            getItemViewType(position) == PEMASUKAN -> {
                (holder as StatistikLayoutAdapter.PemasukanHolder).bind()
            }
            getItemViewType(position) == PENGELUARAN -> {
                (holder as StatistikLayoutAdapter.PengeluaranHolder).bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return listLayout.size
    }

}