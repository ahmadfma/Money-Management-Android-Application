package com.example.moneymanagement.UI.HistoryFragment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.User.UserViewModel

class HistoryViewModel: ViewModel() {

    private val tag = "HistoryViewModel"

    init {
        Log.d(tag, "created")
    }

    interface UI {
        fun setTanggalUI(date: List<String>)
    }

    fun getAllTransactionsDate(viewmodel: UserViewModel, frag: Fragment, ui: HistoryViewModel.UI) {
        viewmodel.getAllDateTransactions()?.observe(frag.viewLifecycleOwner, Observer {
            ui.setTanggalUI(it)
        })
    }

}