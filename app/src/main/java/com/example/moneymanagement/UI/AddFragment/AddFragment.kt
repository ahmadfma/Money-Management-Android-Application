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
import com.example.moneymanagement.UI.HomeFragment.HomeFragment
import com.example.moneymanagement.User.Saldo.SaldoEntity
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
        saldo_user.text = "(Saldo Anda: Rp ${Utilities.formatNumber(HomeFragment.saldo_user)})"
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
            if(kategori != "" && jumlah_saldo.text.toString() != "" && judul.text.toString() != "" && type != "") {
                insertData()
            } else {
                Toast.makeText(context, "Harap Mengisi Semua Kolom", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun insertData() {
        if(type == "pemasukan") {
            val saldoInput = jumlah_saldo.text.toString().toLong()
            viewModel.insertTransaction(TransactionEntity(0, type, kategori, jumlah_saldo.text.toString().toLong(), judul.text.toString(), Utilities.getDate()))
            viewModel.insertUserSaldo(SaldoEntity(0, (HomeFragment.saldo_user+saldoInput), (HomeFragment.pemasukan_user+saldoInput), HomeFragment.pengeluaran_user))
            Toast.makeText(context, "Transaksi Berhasil Disimpan", Toast.LENGTH_SHORT).show()
        } else {
            if(jumlah_saldo.text.toString().toLong() <= HomeFragment.saldo_user) {
                val saldoInput = jumlah_saldo.text.toString().toLong()
                viewModel.insertTransaction(TransactionEntity(0, type, kategori, jumlah_saldo.text.toString().toLong(), judul.text.toString(), Utilities.getDate()))
                viewModel.insertUserSaldo(SaldoEntity(0, (HomeFragment.saldo_user-saldoInput), HomeFragment.pemasukan_user, (HomeFragment.pengeluaran_user+saldoInput)))
                Toast.makeText(context, "Transaksi Berhasil Disimpan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Saldo Anda Tidak Cukup", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddFragment()
        var kategori = ""

    }
}