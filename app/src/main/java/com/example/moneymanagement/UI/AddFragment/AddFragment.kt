package com.example.moneymanagement.UI.AddFragment

import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*

class AddFragment : Fragment() {

    private lateinit var viewModel: UserViewModel

    private val listKategori = arrayListOf<String>(
        "Makanan & Minuman",
        "Kecantikan & Kesehatan",
        "Sosial & Gaya Hidup",
        "Entertainment",
        "Transportasi",
        "Lainnya"
    )

    private val listColorKategori = arrayListOf<Int>(
        R.color.merah,R.color.pink, R.color.ungu, R.color.biru, R.color.kuning, R.color.hijau
    )

    private var type = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvkategori.setHasFixedSize(true)
        rvkategori.layoutManager = LinearLayoutManager(context)
        rvkategori.adapter = KategoriAdapter(listKategori, listColorKategori)
        tipe_pemasukan.setOnClickListener {
            btn_pemasukan.isChecked = true
            btn_pengeluaran.isChecked = false
            type = "pemasukan"
        }
        tipe_pengeluaran.setOnClickListener {
            btn_pengeluaran.isChecked = true
            btn_pemasukan.isChecked = false
            type = "pengeluaran"
        }

        simpan_btn.setOnClickListener {
            insertData()
        }

    }

    private fun insertData() {
        viewModel.insertTransaction(TransactionEntity(0, type, kategori, jumlah_saldo.text.toString().toInt(), judul.text.toString(), Utilities.getDate()))
        Toast.makeText(context, "Transaksi berhasil dimasukkan", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddFragment()
        var kategori = ""

    }
}