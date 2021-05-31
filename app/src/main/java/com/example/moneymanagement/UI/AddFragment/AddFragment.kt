package com.example.moneymanagement.UI.AddFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneymanagement.R
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvkategori.setHasFixedSize(true)
        rvkategori.layoutManager = LinearLayoutManager(context)
        rvkategori.adapter = KategoriAdapter(listKategori, listColorKategori)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AddFragment()
    }
}