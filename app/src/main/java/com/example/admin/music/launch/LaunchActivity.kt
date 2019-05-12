package com.example.admin.music.launch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.admin.music.base.getMyIntent
import com.example.admin.music.login.LoginActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(getMyIntent(this, LoginActivity::class.java))
        finish()
    }
}
