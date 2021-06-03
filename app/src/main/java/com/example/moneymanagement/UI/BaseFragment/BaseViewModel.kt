package com.example.moneymanagement.UI.BaseFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.moneymanagement.R

class BaseViewModel: ViewModel() {

    init {
        Log.d("BaseViewModel", "created")
    }

    var selectedBottomNavigationID = 1

}