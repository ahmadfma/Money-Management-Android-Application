package com.example.moneymanagement.Utilities

import android.text.format.DateFormat
import java.util.*

class Utilities {
    companion object {
        fun getDate(): String {
            var hariIni = ""
            //get Time Now
            val dateNow = Calendar.getInstance().time
            hariIni = DateFormat.format("EEEE", dateNow) as String
            val date = Calendar.getInstance().time
            val tanggal = DateFormat.format("d MMMM yyyy", date) as String
            return "$hariIni, $tanggal"
        }
    }
}