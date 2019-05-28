package com.example.admin.music.network

import com.example.admin.music.base.BasedNetwork
import com.example.admin.music.data.PlaylistDetailBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

object PlaylistNetwork : BasedNetwork() {
    private val playlist: Playlist = retrofit.create(Playlist::class.java)
    fun getDetail(id: String): Call<PlaylistDetailBean.PlaylistDetailData> = playlist.getPlayListDetail(id)
    interface Playlist {
        @GET("/Playlist/detail")
        fun getPlayListDetail(@Query("id") id: String): Call<PlaylistDetailBean.PlaylistDetailData>
    }
}
