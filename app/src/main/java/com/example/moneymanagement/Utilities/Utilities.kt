package com.example.moneymanagement.Utilities

import android.text.format.DateFormat
import android.util.Log
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class Utilities {
    companion object {
        fun getDate(): String {
            var hariIni = ""
            //get Time Now
            val dateNow = Calendar.getInstance().time
            hariIni = DateFormat.format("EEEE", dateNow) as String
            val date = Calendar.getInstance().time
            Log.d("Utilities", "getDate : ${Calendar.getInstance().time}")
            val tanggal = DateFormat.format("d MMMM yyyy", date) as String
            return "$hariIni, $tanggal"
        }

        fun getDate(year: Int, month: Int, day: Int): String {
            Log.d("Utilities", "year : $year")
            Log.d("Utilities", "month : $month")
            Log.d("Utilities", "day : $day")
            val parser =  SimpleDateFormat("yyyy-dd-MM", Locale.US)
            val formatter = SimpleDateFormat("yyyy-dd-MM", Locale.US)
            val formattedDate = formatter.format(parser.parse("$year-$day-$month"))
            val tanggal = DateFormat.format("EEEE, d MMMM yyyy", parser.parse("$year-$day-$month")) as String
            Log.d("Utilities", "tanngal : $tanggal")
            return tanggal
        }

        fun formatNumber(num: Long): String {
            val idLocale: Locale = Locale("id", "ID")
            val format: NumberFormat = NumberFormat.getInstance(idLocale)
            return format.format(num)
        }

    }
}