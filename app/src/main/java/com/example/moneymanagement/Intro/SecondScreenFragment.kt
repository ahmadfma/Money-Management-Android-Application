package com.example.moneymanagement.Intro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.moneymanagement.MainActivity
import com.example.moneymanagement.R
import com.example.moneymanagement.User.Saldo.SaldoEntity
import com.example.moneymanagement.User.UserViewModel
import kotlinx.android.synthetic.main.item_intro_2.*

class SecondScreenFragment : Fragment() {

    var username = ""
    var saldo = ""
    lateinit var viewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.item_intro_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mulaibtn.setOnClickListener {
            if(inputIsFilled()) {
                moveToMainActivity()
            } else {
                Toast.makeText(context, "Harap Mengisi Semua Kolom !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inputIsFilled(): Boolean {
        username = namauser.text.toString()
        saldo = saldouser.text.toString()
        if(username.isNotEmpty() && username.isNotEmpty()) {
            viewModel.insertUserSaldo(SaldoEntity(0, saldo.toLong(),0,0))
            return true
        }
        return false
    }

    private fun moveToMainActivity() {
        val sharedPref = requireActivity().getSharedPreferences("intro", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.putString("username", username)
        editor.apply()

        startActivity(Intent(activity, MainActivity::class.java))
        activity?.finish()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = SecondScreenFragment()
    }
}