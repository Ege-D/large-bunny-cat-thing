package com.ege.firebasetest.versioncheck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ege.firebasetest.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val registerBoolean = intent.getBooleanExtra("register", true)
        homeRegisterBtn.isClickable = registerBoolean
    }
}