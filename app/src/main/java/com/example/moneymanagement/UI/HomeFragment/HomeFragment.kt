package com.example.moneymanagement.UI.HomeFragment

import android.os.Bundle
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
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_transaction_dialog.view.*

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
        viewModel.getLastTransaction()?.observe(viewLifecycleOwner, Observer {
            rvhome.adapter = TransactionsAdapter(it, object : TransactionsAdapter.Listener {
                override fun onViewClick(transaction: TransactionEntity) {
                    onViewAction(transaction)
                }
            })
        })
    }

    private fun onViewAction(transaction: TransactionEntity) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.item_transaction_dialog, null)
        val builder = context?.let {
            AlertDialog.Builder(it)
                .setView(dialogView)
        }
        val mDialog = builder?.show()
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
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}