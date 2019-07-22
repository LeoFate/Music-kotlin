package com.example.admin.music.launch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.admin.music.R
import com.example.admin.music.base.BasedNetwork
import com.example.admin.music.base.getMyIntent
import com.example.admin.music.login.LoginActivity
import java.net.HttpURLConnection
import java.net.URL

class LaunchActivity : AppCompatActivity() {
    private var url = ""
    lateinit var editText: EditText
    lateinit var button: Button
    var isBind = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getUrl()
        ping()
    }

    private fun getUrl() {
        url = getSharedPreferences("launch", 0).getString("url", "")!!
        if (url != "") {
            BasedNetwork.BASE_URL = "http://$url.natappfree.cc"
        }
    }

    private fun ping() {
        Thread(Runnable {
            val urlConnect = URL(BasedNetwork.BASE_URL).openConnection() as HttpURLConnection
            var code: Int
            urlConnect.apply {
                connectTimeout = 3000
                connect()
                code = responseCode
            }
            Log.e("hello", code.toString())
            runOnUiThread {
                if (code == 200) {
                    startActivity(getMyIntent(this, LoginActivity::class.java))
                    if (url != "") {
                        getSharedPreferences("launch", 0).edit().putString("url", url).apply()
                    }
                    finish()
                } else {
                    Toast.makeText(this, "Address can't be access", Toast.LENGTH_SHORT).show()
                    if (!isBind) {
                        bindLayout()
                    }
                }
            }
        }).start()
    }

    private fun bindLayout() {
        setContentView(R.layout.activity_launch)
        editText = findViewById(R.id.launch_et)
        button = findViewById(R.id.launch_b)
        isBind = true
        button.setOnClickListener {
            url = editText.text.toString()
            if (url.length == 6) {
                BasedNetwork.BASE_URL = "http://$url.natappfree.cc"
                ping()
            }
        }
    }
}
