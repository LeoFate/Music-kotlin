package com.example.admin.music.networkservice

import com.example.admin.music.base.BasedNetwork
import com.example.admin.music.data.LoginData
import retrofit2.Call

object LoginNetwork : BasedNetwork() {
    private val login: Api.Login = retrofit.create(Api.Login::class.java)
    fun loginCall(phone: String, password: String): Call<LoginData> = login.login(phone, password)
}