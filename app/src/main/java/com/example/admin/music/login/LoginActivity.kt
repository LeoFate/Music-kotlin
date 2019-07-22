package com.example.admin.music.login

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.admin.music.R
import com.example.admin.music.base.BaseActivity
import com.example.admin.music.base.getMyIntent
import com.example.admin.music.data.LoginData
import com.example.admin.music.launch.LaunchActivity
import com.example.admin.music.main.MainActivity

class LoginActivity : BaseActivity(), LoginContact.View {
    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var button: Button
    private lateinit var checkBox1: CheckBox
    private lateinit var checkBox2: CheckBox
    private var box1: Boolean = false
    private var box2: Boolean = false
    private var phoneNum: String = ""
    private var password: String = ""
    private val loginPresenter = LoginPresenter(this)

    companion object {
        private const val NULL_PASSWORD = "Your password can't be Null."
        private const val NULL_PHONE = "Your phone number can't be Null."
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        layout = R.layout.activity_login
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        editText1 = findViewById(R.id.login_et1)
        editText2 = findViewById(R.id.login_et2)
        button = findViewById(R.id.login_b)
        checkBox1 = findViewById(R.id.login_cb1)
        checkBox2 = findViewById(R.id.login_cb2)
        setBoxListener()
        getMessage()
        setButtonListener()
    }

    override fun setBoxListener() {
        checkBox1.setOnCheckedChangeListener { _, isChecked ->
            val editor = getSharedPreferences("login", 0).edit()
            box1 = isChecked
            editor.putBoolean("box1", box1)
            if (isChecked) {
                checkBox2.isClickable = true
            } else {
                editor.remove("password")
                editor.remove("userName")
                editor.remove("id")
                editor.remove("avatar")
                editor.remove("nickName")
                checkBox2.isClickable = false
                checkBox2.isChecked = false
                editor.putBoolean("box2", box2)
            }
            editor.apply()
        }
        checkBox2.setOnCheckedChangeListener { _, isChecked ->
            val editor = getSharedPreferences("login", 0).edit()
            box2 = isChecked
            editor.putBoolean("box2", box2)
            editor.apply()
        }
    }

    override fun setButtonListener() {
        button.setOnClickListener {
            phoneNum = editText1.text.toString()
            password = editText2.text.toString()
            when {
                phoneNum == "" -> Toast.makeText(this, NULL_PHONE, Toast.LENGTH_SHORT).show()
                password == "" -> Toast.makeText(this, NULL_PASSWORD, Toast.LENGTH_SHORT).show()
                else -> loginPresenter.login(phoneNum, password)
            }
        }
    }

    override fun saveMessage(Code: Int) {
        val editor = getSharedPreferences("login", 0).edit()
        if (Code == 200 || Code == 502 || Code == 509) {
            editor.putString("phone", phoneNum)
        }
        if (box1 && Code == 200) {
            editor.putString("password", password)
        }
        editor.apply()
    }

    override fun getMessage() {
        val sharedPreferences = getSharedPreferences("login", 0)
        box1 = sharedPreferences.getBoolean("box1", false)
        box2 = sharedPreferences.getBoolean("box2", false)
        phoneNum = sharedPreferences.getString("phone", "")
        editText1.setText(phoneNum)
        if (box1) {
            checkBox1.isChecked = true
            password = sharedPreferences.getString("password", "")
            editText2.setText(password)
        }
        if (box2) {
            checkBox2.isChecked = true
            if (callClass == LaunchActivity::class.java.name) {
                loginPresenter.login(phoneNum, password)
            } else if (callClass == MainActivity::class.java.name) {
                Toast.makeText(this, "Log out successfully.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun startMain(loginData: LoginData) {
        val intent = getMyIntent(this, MainActivity::class.java)
        intent.putExtra("userName", loginData.account.userName)
        intent.putExtra("id", loginData.account.id)
        intent.putExtra("avatar", loginData.profile.avatarUrl)
        intent.putExtra("nickName", loginData.profile.nickname)
        startActivity(intent)
        finish()
    }

    override fun startMain(userName: String, id: String, avatar: String, nickName: String) {
        val intent = getMyIntent(this, MainActivity::class.java)
        intent.putExtra("userName", userName)
        intent.putExtra("id", id)
        intent.putExtra("avatar", avatar)
        intent.putExtra("nickName", nickName)
        startActivity(intent)
        finish()
    }
}
