package com.example.admin.music.networkservice

import com.example.admin.music.base.BasedNetwork
import com.example.admin.music.data.PlaylistBean
import retrofit2.Call

object MainNetwork : BasedNetwork() {
    private val main: Api.Main = retrofit.create(Api.Main::class.java)
    fun getPlayList(uid: String): Call<PlaylistBean.PlaylistData> = main.getPlayList(uid)
}
