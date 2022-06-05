package com.example.pashanews.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.pashanews.R
import com.example.pashanews.ui.fragment.HeadLinesCategoryFragment
import dagger.hilt.android.AndroidEntryPoint

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        object: CountDownTimer(2000, 3000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }.start()

    }
}