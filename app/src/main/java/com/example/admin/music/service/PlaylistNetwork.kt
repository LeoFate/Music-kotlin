package com.example.admin.music.service

import com.example.admin.music.data.PlaylistDetailBean
import retrofit2.Call

class PlaylistNetwork private constructor() : BasedNetwork() {
    private val playlist: Api.Playlist = retrofit.create(Api.Playlist::class.java!!)

    fun getDetail(id: String): Call<PlaylistDetailBean.PlaylistDetailData> {
        return playlist.getPlayListDetail(id)
    }

    companion object {
        val instance: PlaylistNetwork
            get() = PlaylistNetwork()
    }
}
