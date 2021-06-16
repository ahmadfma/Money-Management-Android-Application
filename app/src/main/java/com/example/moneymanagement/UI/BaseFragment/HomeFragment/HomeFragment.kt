package com.example.moneymanagement.UI.BaseFragment.HomeFragment

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

const val TAG = "HomeFragment"

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
        tanggal_saat_ini.text = Utilities.getDate()
        saldo.setOnClickListener {
            saldoAction()
        }
        loadUserSaldo()
        loadUserLastTransaction()
    }

    private fun loadUserSaldo() {
        viewModel.getCurrentSaldo()?.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                saldo_user = it
                current_saldo.text = Utilities.formatNumber(it)
            } else {
                saldo_user = 0
                current_saldo.text = "0"
            }
        })

        viewModel.getCurrentPemasukan()?.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                pemasukan_user = it
                pemasukan.text = Utilities.formatNumber(it)
            } else {
                pemasukan_user = 0
                pemasukan.text = "0"
            }
        })

        viewModel.getCurrentPengeluaran()?.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                pengeluaran_user = it
                pengeluaran.text = Utilities.formatNumber(it)
            } else {
                pengeluaran_user = 0
                pengeluaran.text = "0"
            }
        })
    }

    private fun saldoAction() {
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
            viewModel.insertUserSaldo(SaldoEntity(0, s.toLong(), pem.toLong(), peng.toLong()))
            mDialog?.dismiss()
            Toast.makeText(context, "Saldo Berhasil Disimpan", Toast.LENGTH_SHORT).show()
        }
        dialogView.reset_pemasukan.setOnClickListener {
            val saldo = dialogView.saldoET.text.toString().toLong()
            val pengeluaran = dialogView.pengeluaranET.text.toString().toLong()
            viewModel.insertUserSaldo(SaldoEntity(0, saldo, 0, pengeluaran))
            mDialog?.dismiss()
            Toast.makeText(context, "Pemasukan berhasil direset", Toast.LENGTH_SHORT).show()
        }
        dialogView.reset_pengeluaran.setOnClickListener {
            val saldo = dialogView.saldoET.text.toString().toLong()
            val pemasukan = dialogView.pemasukanET.text.toString().toLong()
            viewModel.insertUserSaldo(SaldoEntity(0, saldo, pemasukan, 0))
            mDialog?.dismiss()
            Toast.makeText(context, "Pengeluaran berhasil direset", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUserLastTransaction() {
        viewModel.getLastTransaction()?.observe(viewLifecycleOwner, Observer { //menampilkan riwayat terakhir
            rvhome.adapter = TransactionsAdapter(it, object : TransactionsAdapter.Listener {
                override fun onViewClick(transaction: TransactionEntity) {
                    onViewAction(transaction)
                }
            })
        })
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