package com.example.moneymanagement.UI.AddGoalsFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.AddTransactionFragment.KategoriAdapter
import com.example.moneymanagement.User.Goals.GoalsEntity
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.fragment_add_goals.*

class AddGoalsFragment : Fragment() {

    private lateinit var viewmodel : UserViewModel

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewmodel = ViewModelProvider(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.fragment_add_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvkategori.setHasFixedSize(true)
        rvkategori.layoutManager = LinearLayoutManager(context)
        rvkategori.adapter = KategoriAdapter(listKategori, listColorKategori)

        simpan_btn.setOnClickListener {
            insertData()
        }

    }

    private fun insertData() {
        if(catatan.text.toString() != "" && harga.text.toString() != "" && KategoriAdapter.kategori != "") {
            viewmodel.insertGoals(
                GoalsEntity(
                    0,false,KategoriAdapter.kategori, harga.text.toString().toLong(), catatan.text.toString()
                )
            )
            Toast.makeText(context, "Impian Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Harap Mengisi Semua Kolom", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = AddGoalsFragment()
    }
}