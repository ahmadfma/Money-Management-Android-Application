package com.example.moneymanagement.Intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.moneymanagement.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    private val fragmentList = arrayListOf<Fragment>(
        FirstScreenFragment(),
        SecondScreenFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        supportActionBar?.hide()

        viewpager.adapter = IntroAdapter(fragmentList, supportFragmentManager, lifecycle)
        circleindicator.setViewPager(viewpager)
    }
}