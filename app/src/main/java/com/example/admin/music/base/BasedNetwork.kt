package com.example.admin.music.base

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BasedNetwork {
    companion object {
        var BASE_URL = "http://7wg7ck.natappfree.cc"
    }

    protected val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
}
