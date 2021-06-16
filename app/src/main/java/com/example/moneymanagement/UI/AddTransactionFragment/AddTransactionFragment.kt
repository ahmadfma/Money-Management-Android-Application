package com.example.moneymanagement.UI.AddTransactionFragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.BaseFragment.GoalsFragment.GoalsFragment
import com.example.moneymanagement.UI.BaseFragment.HomeFragment.HomeFragment
import com.example.moneymanagement.User.Saldo.SaldoEntity
import com.example.moneymanagement.User.TransactionData.TransactionEntity
import com.example.moneymanagement.User.UserViewModel
import com.example.moneymanagement.Utilities.Utilities
import kotlinx.android.synthetic.main.fragment_add_transaction.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.*

const val TAG = "AddTransactionFragment"

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
        toolbarTransaksi.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        rvkategori.setHasFixedSize(true)
        rvkategori.layoutManager = LinearLayoutManager(context)
        rvkategori.adapter = KategoriAdapter(listKategori, listColorKategori)
        saldolayout.helperText = "Saldo Anda: Rp ${Utilities.formatNumber(HomeFragment.saldo_user)}"

        tanggal.setOnClickListener {
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

        if(action == "add") {
            tanggal.setText(Utilities.getDate())
            addAction()
        } else { //update
            updateAction()
        }

    }

    private fun addAction() {
        simpan_btn.setOnClickListener {
            if(KategoriAdapter.kategori != "" && jumlah_saldo.text.toString() != "" && judul.text.toString() != "" && type != "") {
                insertData()
                activity?.onBackPressed()
            } else {
                Toast.makeText(context, "Harap Mengisi Semua Kolom", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAction() {
        hapus_btn.visibility = View.VISIBLE
        hapus_kembalikan_btn.visibility = View.VISIBLE
        when(selected_transaction?.category) {
            "Makanan & Minuman" -> {
                KategoriAdapter.selected_radio_btn = 0
            }
            "Kecantikan & Kesehatan" -> {
                KategoriAdapter.selected_radio_btn = 1
            }
            "Sosial & Gaya Hidup" -> {
                KategoriAdapter.selected_radio_btn = 2
            }
            "Entertainment" -> {
                KategoriAdapter.selected_radio_btn = 3
            }
            "Transportasi" -> {
                KategoriAdapter.selected_radio_btn = 4
            }
            "Lainnya" -> {
                KategoriAdapter.selected_radio_btn = 5
            }
        }
        judul.setText(selected_transaction?.title)
        tanggal.setText(selected_transaction?.date)
        jumlah_saldo.setText(selected_transaction?.amount.toString())
        when(selected_transaction?.type) {
            "pemasukan" -> {
                btn_pemasukan.isChecked = true
                type = "pemasukan"
            } else -> {
                btn_pengeluaran.isChecked = true
                type = "pengeluaran"
            }
        }
        simpan_btn.text = "Update Transaksi"
        simpan_btn.setOnClickListener {
            if(KategoriAdapter.kategori != "" && jumlah_saldo.text.toString() != "" && judul.text.toString() != "" && type != "") {
                updateData()
                activity?.onBackPressed()
            } else {
                Toast.makeText(context, "Harap Mengisi Semua Kolom", Toast.LENGTH_SHORT).show()
            }
        }
        hapus_btn.setOnClickListener {
            deleteData()
            activity?.onBackPressed()
        }
        hapus_kembalikan_btn.setOnClickListener {
            deleteAndReturnSaldo()
            activity?.onBackPressed()
        }
    }

    private fun deleteAndReturnSaldo() {
        when(selected_transaction?.type) {
            "pemasukan" -> {
                viewModel.insertUserSaldo(SaldoEntity(0, (HomeFragment.saldo_user - selected_transaction!!.amount), (HomeFragment.pemasukan_user -selected_transaction!!.amount),
                    HomeFragment.pengeluaran_user
                ))
            }
            "pengeluaran" -> {
                viewModel.insertUserSaldo(SaldoEntity(0, (HomeFragment.saldo_user +selected_transaction!!.amount),
                    HomeFragment.pemasukan_user, (HomeFragment.pengeluaran_user -selected_transaction!!.amount) ))
            }
        }
        viewModel.deleteTransactions(selected_transaction!!)
        Toast.makeText(context, "Transaksi dihapus dan saldo dikembalikan", Toast.LENGTH_SHORT).show()
    }

    private fun deleteData() {
        viewModel.deleteTransactions(selected_transaction!!)
        Toast.makeText(context, "Transaksi Berhasil Dihapus", Toast.LENGTH_SHORT).show()
    }

    private fun updateData() {
        if(selected_transaction?.type == "pemasukan") { // cek type transaksi yang dipilih

            val saldoInput = jumlah_saldo.text.toString().toLong()
            if(type == "pemasukan") { // pemasukan -> pemasukan
                HomeFragment.saldo_user = (HomeFragment.saldo_user-selected_transaction?.amount!!+saldoInput)
                viewModel.insertUserSaldo(SaldoEntity(0,
                    checkIfNotBelowZero(HomeFragment.saldo_user),
                    checkIfNotBelowZero((HomeFragment.pemasukan_user-selected_transaction?.amount!!+saldoInput)),
                    checkIfNotBelowZero(HomeFragment.pengeluaran_user)
                ))
                Log.d(TAG, "pemasukan -> pemasukan")
            } else { //jika type transaksi yang dipilih diubah (pemasukan -> pengeluaran) maka jalankan program dibawah
                //temp = saldo saat ini dikurang dengan jumlah uang dari transaksi yang dipilih
                //temp = temp - inputan saldo
                //pengeluaran = pengeluaran + inputan saldo
                //pemasukan = pemasukan - jumlah uang dari transaksi yang dipilih
                Log.d(TAG, "pemasukan -> pemasukan")
                Log.d(TAG, "saldo user : ${HomeFragment.saldo_user}")
                Log.d(TAG, "selected transaksi amount : ${selected_transaction?.amount}")
                Log.d(TAG, "saldo input : $saldoInput")
                Log.d(TAG, "saldo user - selected transaksi amount - saldo input")
                HomeFragment.saldo_user = HomeFragment.saldo_user - selected_transaction?.amount!!
                HomeFragment.saldo_user = HomeFragment.saldo_user - saldoInput
                Log.d(TAG, "saldo user = ${HomeFragment.saldo_user}")
                HomeFragment.pemasukan_user = HomeFragment.pemasukan_user - selected_transaction?.amount!!
                HomeFragment.pengeluaran_user = HomeFragment.pengeluaran_user + saldoInput
                viewModel.insertUserSaldo(SaldoEntity(0,
                    checkIfNotBelowZero(HomeFragment.saldo_user),
                    checkIfNotBelowZero(HomeFragment.pemasukan_user),
                    checkIfNotBelowZero(HomeFragment.pengeluaran_user)
                ))

            }
            viewModel.updateTransactions(TransactionEntity(selected_transaction?.id!!, type, KategoriAdapter.kategori, jumlah_saldo.text.toString().toLong(), judul.text.toString(), tanggal.text.toString()))
            Toast.makeText(context, "Transaksi Berhasil Diubah", Toast.LENGTH_SHORT).show()

        } else {

            val saldoInput = jumlah_saldo.text.toString().toLong()
            if(type == "pengeluaran") { //pengeluaran -> pengeluaran
                if(saldoInput <= HomeFragment.saldo_user) {
                    HomeFragment.saldo_user = selected_transaction?.amount!!-saldoInput + HomeFragment.saldo_user
                    viewModel.insertUserSaldo(SaldoEntity(0,
                        checkIfNotBelowZero(HomeFragment.saldo_user),
                        checkIfNotBelowZero(HomeFragment.pemasukan_user),
                        checkIfNotBelowZero((HomeFragment.pengeluaran_user-selected_transaction?.amount!!+saldoInput))
                    ))
                    viewModel.updateTransactions(TransactionEntity(selected_transaction?.id!!, type, KategoriAdapter.kategori, jumlah_saldo.text.toString().toLong(), judul.text.toString(), tanggal.text.toString()))
                    Toast.makeText(context, "Transaksi Berhasil Diubah", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Saldo Anda Tidak Cukup", Toast.LENGTH_SHORT).show()
                }
            } else { //jika type transaksi yang dipilih diubah (pengeluaran -> pemasukan) maka jalankan program dibawah
                /*
                 * pengeluaran -> pemasukan
                 * saldo saat ini = saldo + jumlah uang dari transaksi yang pilih
                 * saldo saat ini = saldo + jumlah inputan saldo
                 * pengeluaran = pengeluaran - jumlah uang dari transaksi yang pilih
                 * pemasukan = pemasukan + jumlah inputan saldo
                 */
                Log.d(TAG, "pengeluaran -> pemasukan")
                Log.d(TAG, "saldo user : ${HomeFragment.saldo_user}")
                Log.d(TAG, "selected transaksi amount : ${selected_transaction?.amount}")
                Log.d(TAG, "saldo input : $saldoInput")
                Log.d(TAG, "saldo user + selected transaksi amount + saldo input")
                HomeFragment.saldo_user = HomeFragment.saldo_user + selected_transaction?.amount!! + saldoInput
                Log.d(TAG, "saldo user = ${HomeFragment.saldo_user}")

                HomeFragment.pengeluaran_user = HomeFragment.pengeluaran_user - selected_transaction?.amount!!
                HomeFragment.pemasukan_user = HomeFragment.pemasukan_user + saldoInput
                viewModel.insertUserSaldo(SaldoEntity(0,
                    checkIfNotBelowZero(HomeFragment.saldo_user),
                    checkIfNotBelowZero(HomeFragment.pemasukan_user),
                    checkIfNotBelowZero(HomeFragment.pengeluaran_user)
                ))
                viewModel.updateTransactions(TransactionEntity(selected_transaction?.id!!, type, KategoriAdapter.kategori, jumlah_saldo.text.toString().toLong(), judul.text.toString(), tanggal.text.toString()))
                Toast.makeText(context, "Transaksi Berhasil Diubah", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertData() {
        if(type == "pemasukan") {
            val saldoInput = jumlah_saldo.text.toString().toLong()
            viewModel.insertTransaction(TransactionEntity(getID(), type, KategoriAdapter.kategori, jumlah_saldo.text.toString().toLong(), judul.text.toString(), tanggal.text.toString()))
            viewModel.insertUserSaldo(SaldoEntity(0, (HomeFragment.saldo_user+saldoInput), (HomeFragment.pemasukan_user+saldoInput), HomeFragment.pengeluaran_user))
            Toast.makeText(context, "Transaksi Berhasil Disimpan", Toast.LENGTH_SHORT).show()
        } else {
            if(jumlah_saldo.text.toString().toLong() <= HomeFragment.saldo_user) {
                val saldoInput = jumlah_saldo.text.toString().toLong()
                viewModel.insertTransaction(TransactionEntity(getID(), type, KategoriAdapter.kategori, jumlah_saldo.text.toString().toLong(), judul.text.toString(), tanggal.text.toString()))
                viewModel.insertUserSaldo(SaldoEntity(0, (HomeFragment.saldo_user-saldoInput), HomeFragment.pemasukan_user, (HomeFragment.pengeluaran_user+saldoInput)))
                Toast.makeText(context, "Transaksi Berhasil Disimpan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Saldo Anda Tidak Cukup", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfNotBelowZero(num: Long): Long {
        return if(num < 0) {
            0
        } else {
            num
        }
    }

    private fun getDate(et: EditText) {
        val cal = Calendar.getInstance()
        val yearr = cal.get(Calendar.YEAR)
        val monthh = cal.get(Calendar.MONTH)
        val dayy = cal.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            m = (month+1).toString()
            y = year.toString()
            d = dayOfMonth.toString()
            if(m.length == 1) {
                m = "0$m"
            }
            if(d.length == 1) {
                d = "0$d"
            }
            et.setText(Utilities.getDate(year, (month+1), dayOfMonth))
        }, yearr, monthh, dayy)
        dpd.show()
    }

    private fun getID(): String {
        val date = Calendar.getInstance().time
        val jam = DateFormat.format("HHmmss", date) as String
        return "$y$m$d$jam"
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddTransactionFragment()
        var m = ""
        var y = ""
        var d = ""
        var kategori = ""
        var action = ""
        var selected_transaction: TransactionEntity? = null

    }
}