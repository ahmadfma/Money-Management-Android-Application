package com.example.moneymanagement.UI.BaseFragment

import android.util.Log
import androidx.lifecycle.ViewModel

class BaseViewModel: ViewModel() {

    init {
        Log.d("BaseViewModel", "created")
    }

    var selectedBottomNavigationID = 0

}