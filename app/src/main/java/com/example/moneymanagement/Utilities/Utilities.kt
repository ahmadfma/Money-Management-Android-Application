package com.example.moneymanagement.Utilities

import android.graphics.drawable.Drawable
import android.text.format.DateFormat
import android.util.Log
import com.example.moneymanagement.R
import com.example.moneymanagement.UI.AddTransactionFragment.AddTransactionFragment
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Utilities {
    companion object {
        fun getDate(): String {
            var hariIni = ""
            //get Time Now
            val dateNow = Calendar.getInstance().time
            hariIni = DateFormat.format("EEEE", dateNow) as String
            val date = Calendar.getInstance().time
            Log.d("Utilities", "getDate : ${Calendar.getInstance().time}")
            AddTransactionFragment.d = DateFormat.format("d", date) as String
            AddTransactionFragment.m = DateFormat.format("MM", date) as String
            AddTransactionFragment.y = DateFormat.format("yyyy", date) as String
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

        fun listKateogri(): ArrayList<String> {
            return arrayListOf(
                "Makanan & Minuman",
                "Kecantikan & Kesehatan",
                "Sosial & Gaya Hidup",
                "Entertainment",
                "Transportasi",
                "Lainnya"
            )
        }

        fun listKateogriColor(): ArrayList<Int> {
            return arrayListOf<Int>(
                R.color.merah,
                R.color.pink,
                R.color.ungu,
                R.color.biru,
                R.color.kuning,
                R.color.hijau
            )
        }
        fun listKategoriIcon(type: String): ArrayList<Int> {
            when(type) {
                "GOAL" -> {
                    return arrayListOf(
                        R.drawable.ic_fast_food,
                        R.drawable.ic_healthy,
                        R.drawable.ic_makeup,
                        R.drawable.ic_drum_set,
                        R.drawable.ic_transportation,
                        R.drawable.ic_goal
                    )
                }
                else -> {
                    return arrayListOf(
                        R.drawable.ic_fast_food,
                        R.drawable.ic_healthy,
                        R.drawable.ic_makeup,
                        R.drawable.ic_drum_set,
                        R.drawable.ic_transportation,
                        R.drawable.ic_transaction
                    )
                }
            }
        }
    }
}