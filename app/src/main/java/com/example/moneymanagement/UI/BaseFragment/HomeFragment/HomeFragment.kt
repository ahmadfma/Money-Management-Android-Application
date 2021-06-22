package com.example.moneymanagement.UI.BaseFragment.HomeFragment

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.AddTransactionFragment.AddTransactionFragment
import com.example.moneymanagement.User.Saldo.SaldoEntity
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_saldo_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private val listLayout = arrayListOf<String>(
        "CARD",
        "LAST TRANSACTION",
        "STATISTIC PEMASUKAN",
        "STATISTIC PENGELUARAN"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setGreeting()
        tanggal_saat_ini.text = Utilities.getDate()
        loadUI()

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setBackgroundColorOfTopBar(2.0f)
                    changeColor()
                }
            }
        }
        tes()
    }

    private fun setGreeting() {
        val simpleDateFormat = SimpleDateFormat("HH")
        val currentTime: String = simpleDateFormat.format(Date())
        Log.d("setGreeting", "$currentTime")
        val time = currentTime.toLong()
        when (time) {
            in 0..11 -> {
                greeting.text = "Selamat Pagi,"
            }
            in 12..14 -> {
                greeting.text = "Selamat Siang,"
            }
            in 15..18 -> {
                greeting.text = "Selamat Sore,"
            }
            in 19..23 -> {
                greeting.text = "Selamat Malam,"
            }
        }
    }

    private fun loadUI() = lifecycleScope.launch {
        withContext(Dispatchers.Main) {
            recyclerview.setHasFixedSize(true)
            recyclerview.layoutManager = LinearLayoutManager(context)
            recyclerview.adapter = LayoutAdapter(listLayout, info_transaksi_belum_ada, viewModel, this@HomeFragment, object : LayoutAdapter.Listener {
                override fun onCardClick(
                    saldo: TextView,
                    pemasukan: TextView,
                    pengeluaran: TextView
                ) {
                    saldoAction(saldo, pemasukan, pengeluaran)
                }

                override fun onTransactionViewClick(transactionEntity: TransactionEntity) {
                    onViewAction(transactionEntity)
                }
            })
        }
    }

    private fun tes() = lifecycleScope.launch(Dispatchers.IO) {
        val pem = viewModel.getTotalAmount("pemasukan")
        val peng = viewModel.getTotalAmount("pengeluaran")
        Log.d("DEBUGING", "total pemasukan : $pem")
        Log.d("DEBUGING", "total pengeluaran : $peng")
        Utilities.listKateogri().forEach {
            val saldo = async {
                viewModel.getTotalAmountByCategory(it, "pemasukan")
            }.await()
            Log.d("DEBUGING", "total pemasukan kategori $it : $saldo")
        }
        Utilities.listKateogri().forEach {
            val saldo = async {
                viewModel.getTotalAmountByCategory(it, "pengeluaran")
            }.await()
            Log.d("DEBUGING", "total pengeluaran kategori $it : $saldo")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun changeColor() {
        var floatvalue = 1f
        var totalvalue = 550
        var isTopChangeColor = false
        var isTopChangeTranslationZChange = false
        recyclerview.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(oldScrollY < 0) { //scroll ke bawah
                totalvalue -= abs(oldScrollY)
                if(totalvalue >= 0) {
                    if(floatvalue > 0) {
                        floatvalue -= abs(oldScrollY.toFloat()/400f)
                        if(floatvalue < 0f) {
                            floatvalue = 0f
                        }
                    }
                }
                if(floatvalue == 0f) {
                    if(!isTopChangeColor) {
                        isTopChangeColor = true
                        top.setBackgroundResource(R.drawable.user_background)
                    }
                }
                if(totalvalue < 550) {
                    if(!isTopChangeTranslationZChange) {
                        Log.d("TOP TRANSLATION Z", "change")
                        top.translationZ = 8F
                        isTopChangeTranslationZChange = true
                    }
                }
            } else { //scroll ke atas
                totalvalue += abs(oldScrollY)
                if(totalvalue > 550) {
                    totalvalue = 550
                }
                if(floatvalue < 1.0f) {
                    floatvalue += abs(oldScrollY.toFloat()/400f)
                    if(floatvalue > 1f) {
                        floatvalue = 1f
                    }
                }
            }

            if(totalvalue < 0 && floatvalue != 0.0f) {
                setBackgroundColorOfTopBar(0.0f)
                floatvalue = 0.0f
            }

            if(totalvalue in 1..550) {
                Log.d("ScrollChangeListener", "float value : $floatvalue")
                Log.d("ScrollChangeListener", "total value : $totalvalue")
                setBackgroundColorOfTopBar(floatvalue)
            }

            if(totalvalue == 550) {
                setBackgroundColorOfTopBar(2.0f)
                top.setBackgroundResource(R.color.primary)
                top.translationZ = 0F
                isTopChangeColor = false
                isTopChangeTranslationZChange = false
                Log.d("TOP TRANSLATION Z", "back")
            }


        }
    }

    private fun setBackgroundColorOfTopBar(value: Float) {
//        Log.d("Top Bar Home", "setBackgroundColorOfTopBar, float value : $value")
        when (value) {
            in 0.0..0.1 -> {
                setColor(R.color.p_00)
            }
            in 0.1..0.2 -> {
                setColor(R.color.p_01)
            }
            in 0.2..0.3 -> {
                setColor(R.color.p_02)
            }
            in 0.3..0.4 -> {
                setColor(R.color.p_03)
            }
            in 0.4..0.5 -> {
                setColor(R.color.p_04)
            }
            in 0.5..0.6 -> {
                setColor(R.color.p_05)
            }
            in 0.6..0.7 -> {
                setColor(R.color.p_06)
            }
            in 0.7..0.8 -> {
                setColor(R.color.p_07)
            }
            in 0.8..0.9 -> {
                setColor(R.color.p_08)
            }
            in 0.9..1.0 -> {
                setColor(R.color.p_09)
            }
            else -> {
                setColor(R.color.p_1)
            }
        }
    }

    private fun setColor(id: Int) {
        val gradientDrawable = topback.background.mutate() as GradientDrawable
        gradientDrawable.setColor(ResourcesCompat.getColor(resources, id, null))
    }

    private fun saldoAction(user_saldo: TextView, pemasukan: TextView, pengeluaran: TextView) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.item_saldo_dialog, null)
        val builder = context?.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
        }
        val mDialog = builder?.show()
        dialogView.saldoET.setText(saldo_user.toString())
        dialogView.pemasukanET.setText(pemasukan_user.toString())
        dialogView.pengeluaranET.setText(pengeluaran_user.toString())
        dialogView.close_btn.setOnClickListener {
            mDialog?.dismiss()
        }
        dialogView.simpan.setOnClickListener {
            var s = dialogView.saldoET.text.toString()
            var pem = dialogView.pemasukanET.text.toString()
            var peng = dialogView.pengeluaranET.text.toString()
            if(s.isEmpty()) {
                s = "0"
            }
            if(pem.isEmpty()) {
                pem = "0"
            }
            if(peng.isEmpty()) {
                peng = "0"
            }

            user_saldo.text = Utilities.formatNumber(s.toLong())
            pemasukan.text = Utilities.formatNumber(pem.toLong())
            pengeluaran.text = Utilities.formatNumber(peng.toLong())

            viewModel.insertUserSaldo(SaldoEntity(0, s.toLong(), pem.toLong(), peng.toLong()))
            mDialog?.dismiss()
            Toast.makeText(context, "Saldo Berhasil Disimpan", Toast.LENGTH_SHORT).show()

            saldo_user = s.toLong()
            pemasukan_user = pem.toLong()
            pengeluaran_user = peng.toLong()
        }
        dialogView.reset_pemasukan.setOnClickListener {
            val saldo = dialogView.saldoET.text.toString()
            val pengeluaran_user = dialogView.pengeluaranET.text.toString()

            user_saldo.text = Utilities.formatNumber(saldo.toLong())
            pemasukan.text = "0"
            pengeluaran.text = Utilities.formatNumber(pengeluaran_user.toLong())

            viewModel.insertUserSaldo(SaldoEntity(0, saldo.toLong(), 0, pengeluaran_user.toLong()))
            mDialog?.dismiss()
            Toast.makeText(context, "Pemasukan berhasil direset", Toast.LENGTH_SHORT).show()

            saldo_user = saldo.toLong()
            pemasukan_user = 0
            Companion.pengeluaran_user = pengeluaran_user.toLong()
        }
        dialogView.reset_pengeluaran.setOnClickListener {
            val saldo = dialogView.saldoET.text.toString()
            val pemasukan_user = dialogView.pemasukanET.text.toString()

            user_saldo.text = Utilities.formatNumber(saldo.toLong())
            pemasukan.text = Utilities.formatNumber(pemasukan_user.toLong())
            pengeluaran.text = "0"

            viewModel.insertUserSaldo(SaldoEntity(0, saldo.toLong(), pemasukan_user.toLong(), 0))
            mDialog?.dismiss()
            Toast.makeText(context, "Pengeluaran berhasil direset", Toast.LENGTH_SHORT).show()

            saldo_user = saldo.toLong()
            Companion.pemasukan_user = pemasukan_user.toLong()
            pengeluaran_user = 0
        }
    }

    private fun onViewAction(transaction: TransactionEntity) {
        AddTransactionFragment.selected_transaction = transaction
        AddTransactionFragment.action = "update"
        findNavController().navigate(R.id.action_baseFragment_to_addTransactionFragment)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
        var saldo_user = 0L
        var pemasukan_user = 0L
        var pengeluaran_user = 0L
    }
}