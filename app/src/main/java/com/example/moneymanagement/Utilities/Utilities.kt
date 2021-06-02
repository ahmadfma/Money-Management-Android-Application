package com.example.moneymanagement.Utilities

import android.text.format.DateFormat
import java.text.NumberFormat
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

        fun formatNumber(num: Long): String {
            val idLocale: Locale = Locale("id", "ID")
            val format: NumberFormat = NumberFormat.getInstance(idLocale)
            return format.format(num)
        }

    }
}