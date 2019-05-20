package com.example.admin.music.networkservice

import com.example.admin.music.base.BasedNetwork
import com.example.admin.music.data.SongCheck
import com.example.admin.music.data.SongData
import retrofit2.Call

object SongNetwork : BasedNetwork() {
    private val song: Api.Song = retrofit.create(Api.Song::class.java)
    fun getSongUrl(id: String): Call<SongData> = song.getSongUrl(id)
    fun accessible(id: String): Call<SongCheck> = song.accessible(id)
}