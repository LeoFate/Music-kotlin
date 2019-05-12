package com.example.admin.music.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BasedNetwork {

    protected val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()


    companion object {
        private const val BASE_URL = "http://192.168.43.10:3000"
    }
}
