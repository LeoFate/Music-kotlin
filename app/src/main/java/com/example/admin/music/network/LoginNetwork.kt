package com.example.admin.music.network

import com.example.admin.music.base.BasedNetwork
import com.example.admin.music.data.LoginData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

object LoginNetwork : BasedNetwork() {
    private val login: Login = retrofit.create(Login::class.java)
    fun loginCall(phone: String, password: String): Call<LoginData> = login.login(phone, password)
    interface Login {
        @GET("/login/cellphone")
        fun login(@Query("phone") phone: String, @Query("password") password: String): Call<LoginData>
    }
}