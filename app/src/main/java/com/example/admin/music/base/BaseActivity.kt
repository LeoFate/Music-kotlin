package com.example.admin.music.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    protected var layout = 0
    protected lateinit var callClass: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callClass = getCallClass(intent)
        setContentView(layout)
        initView()
    }

    abstract fun initView()
}

