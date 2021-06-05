package com.example.moneymanagement.UI.AddTransactionFragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.HomeFragment.HomeFragment
import com.example.moneymanagement.User.Saldo.SaldoEntity
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import java.util.*

class AddTransactionFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_add_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvkategori.setHasFixedSize(true)
        rvkategori.layoutManager = LinearLayoutManager(context)
        rvkategori.adapter = KategoriAdapter(listKategori, listColorKategori)
        saldo_user.text = "(Saldo Anda: Rp ${Utilities.formatNumber(HomeFragment.saldo_user)})"
        tanggal.setText(Utilities.getDate())
        tanggal.setOnClickListener {
            Toast.makeText(context, "tanggal", Toast.LENGTH_SHORT).show()
            getDate(tanggal)
        }
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

    private fun getDate(et: EditText) {
        val cal = Calendar.getInstance()
        val yearr = cal.get(Calendar.YEAR)
        val monthh = cal.get(Calendar.MONTH)
        val dayy = cal.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            et.setText(Utilities.getDate(year, (month+1), dayOfMonth))
        }, yearr, monthh, dayy)
        dpd.show()
    }

    private fun insertData() {
        if(type == "pemasukan") {
            val saldoInput = jumlah_saldo.text.toString().toLong()
            viewModel.insertTransaction(TransactionEntity(0, type, kategori, jumlah_saldo.text.toString().toLong(), judul.text.toString(), tanggal.text.toString()))
            viewModel.insertUserSaldo(SaldoEntity(0, (HomeFragment.saldo_user+saldoInput), (HomeFragment.pemasukan_user+saldoInput), HomeFragment.pengeluaran_user))
            Toast.makeText(context, "Transaksi Berhasil Disimpan", Toast.LENGTH_SHORT).show()
        } else {
            if(jumlah_saldo.text.toString().toLong() <= HomeFragment.saldo_user) {
                val saldoInput = jumlah_saldo.text.toString().toLong()
                viewModel.insertTransaction(TransactionEntity(0, type, kategori, jumlah_saldo.text.toString().toLong(), judul.text.toString(), tanggal.text.toString()))
                viewModel.insertUserSaldo(SaldoEntity(0, (HomeFragment.saldo_user-saldoInput), HomeFragment.pemasukan_user, (HomeFragment.pengeluaran_user+saldoInput)))
                Toast.makeText(context, "Transaksi Berhasil Disimpan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Saldo Anda Tidak Cukup", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddTransactionFragment()
        var kategori = ""

    }
}