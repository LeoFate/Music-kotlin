package com.example.admin.music.login

import com.example.admin.music.data.LoginData

interface LoginContact {
    interface View {
        fun setBoxListener()
        fun setButtonListener()
        fun saveMessage(Code: Int)
        fun getMessage()
        fun startMain(loginData: LoginData)
        fun startMain(userName: String, id: String, avatar: String, nickName: String)
    }

    interface Presenter {
        fun login(phone: String, password: String)
    }
}
