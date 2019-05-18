package com.example.admin.music.service

import com.example.admin.music.data.SongCheck
import com.example.admin.music.data.SongData
import retrofit2.Call

class SongNetwork : BasedNetwork() {
    private val song: Api.Song = retrofit.create(Api.Song::class.java)
    fun getSongUrl(id: String): Call<SongData> = song.getSongUrl(id)
    fun accessible(id: String): Call<SongCheck> = song.accessible(id)

    companion object {
        val instance: SongNetwork
            get() = SongNetwork()
    }
}