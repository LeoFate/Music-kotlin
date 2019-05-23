package com.example.admin.music.networkservice

import com.example.admin.music.base.BasedNetwork
import com.example.admin.music.data.PlaylistBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

object MainNetwork : BasedNetwork() {
    private val main: Main = retrofit.create(Main::class.java)
    fun getPlayList(uid: String): Call<PlaylistBean.PlaylistData> = main.getPlayList(uid)
    interface Main {
        @GET("/user/Playlist")
        fun getPlayList(@Query("uid") uid: String): Call<PlaylistBean.PlaylistData>
    }
}
