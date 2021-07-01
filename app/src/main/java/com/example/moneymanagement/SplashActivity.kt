package com.example.moneymanagement

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import com.example.moneymanagement.Intro.IntroActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        lifecycleScope.launch {
            delay(2000)
            if(!isFirstLaunched()) {
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }

    }

    private fun isFirstLaunched(): Boolean {
        val sharedPref = this.getSharedPreferences("intro", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

}