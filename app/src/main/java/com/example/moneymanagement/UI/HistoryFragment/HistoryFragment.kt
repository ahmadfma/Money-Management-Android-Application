package com.example.moneymanagement.UI.HistoryFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.HistoryFragment.Adapter.TombolTanggalAdapter
import com.example.moneymanagement.UI.HomeFragment.TransactionsAdapter
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.item_transaction_dialog.view.*
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
                if(date.isNotEmpty()) {
                    setTombolTanggal(date)
                    loadTransactionBasedOnDate(date[0])
                }
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
        val dialogView = LayoutInflater.from(context).inflate(R.layout.item_transaction_dialog, null)
        val builder = context?.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
        }
        val mDialog = builder?.show()
        when(transaction.category) {
            "Makanan & Minuman" -> {
                dialogView.line.setImageResource(R.color.merah)
            }
            "Kecantikan & Kesehatan" -> {
                dialogView.line.setImageResource(R.color.pink)
            }
            "Sosial & Gaya Hidup"-> {
                dialogView.line.setImageResource(R.color.ungu)
            }
            "Entertainment" -> {
                dialogView.line.setImageResource(R.color.biru)
            }
            "Transportasi" -> {
                dialogView.line.setImageResource(R.color.kuning)
            }
            "Lainnya" -> {
                dialogView.line.setImageResource(R.color.hijau)
            }
        }
        dialogView.close_btn.setOnClickListener {
            mDialog?.dismiss()
        }
        dialogView.judulET.setText(transaction.title)
        dialogView.jumlahET.setText(transaction.amount.toString())
        dialogView.tanggalET.isEnabled = false
        dialogView.tanggalET.setText(transaction.date)
        dialogView.tipeET.isEnabled = false
        dialogView.tipeET.setText(transaction.type)
        when(transaction.category) {
            "Makanan & Minuman" -> {
                dialogView.makanan_minuman.isChecked = true
            }
            "Kecantikan & Kesehatan" -> {
                dialogView.kecantikan_kesehatan.isChecked = true
            }
            "Sosial & Gaya Hidup" -> {
                dialogView.sosial_gayahidup.isChecked = true
            }
            "Entertainment" -> {
                dialogView.entertainment.isChecked = true
            }
            "Transportasi" -> {
                dialogView.transportasi.isChecked = true
            }
            "Lainnya" -> {
                dialogView.lainnya.isChecked = true
            }
        }

        dialogView.simpan.setOnClickListener {
            var kategori = ""
            when(dialogView.radiogroup.checkedRadioButtonId) {
                R.id.makanan_minuman -> {
                    kategori = "Makanan & Minuman"
                }
                R.id.kecantikan_kesehatan -> {
                    kategori = "Kecantikan & Kesehatan"
                }
                R.id.sosial_gayahidup -> {
                    kategori = "Sosial & Gaya Hidup"
                }
                R.id.entertainment -> {
                    kategori = "Entertainment"
                }
                R.id.transportasi -> {
                    kategori = "Transportasi"
                }
                R.id.lainnya -> {
                    kategori = "Lainnya"
                }
            }

            viewmodel_user.updateTransactions(TransactionEntity(transaction.id,
                transaction.type,
                kategori,
                dialogView.jumlahET.text.toString().toInt(),
                dialogView.judulET.text.toString(),
                transaction.date
            ))
            mDialog?.dismiss()
            Toast.makeText(context, "Berhasil Disimpan", Toast.LENGTH_SHORT).show()
        }

        dialogView.hapus.setOnClickListener {
            viewmodel_user.deleteTransactions(transaction)
            mDialog?.dismiss()
            Toast.makeText(context, "Berhasil Dihapus", Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}