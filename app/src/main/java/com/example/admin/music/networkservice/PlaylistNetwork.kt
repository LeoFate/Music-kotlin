package com.example.admin.music.networkservice

import com.example.admin.music.base.BasedNetwork
import com.example.admin.music.data.PlaylistDetailBean
import retrofit2.Call

object PlaylistNetwork : BasedNetwork() {
    private val playlist: Api.Playlist = retrofit.create(Api.Playlist::class.java)
    fun getDetail(id: String): Call<PlaylistDetailBean.PlaylistDetailData> = playlist.getPlayListDetail(id)
}
