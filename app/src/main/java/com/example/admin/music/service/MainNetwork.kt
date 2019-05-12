package com.example.admin.music.service

import com.example.admin.music.data.PlaylistBean
import retrofit2.Call

class MainNetwork private constructor() : BasedNetwork() {
    private val main: Api.Main = retrofit.create(Api.Main::class.java)

    fun getPlayList(uid: String): Call<PlaylistBean.PlaylistData> {
        return main.getPlayList(uid)
    }

    companion object {
        val instance: MainNetwork
            get() = MainNetwork()
    }
}
