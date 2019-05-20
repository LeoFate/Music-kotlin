package com.example.admin.music.base

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BasedNetwork {
    companion object {
        private const val BASE_URL = "http://192.168.43.10:3000"
    }

    protected val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
}
