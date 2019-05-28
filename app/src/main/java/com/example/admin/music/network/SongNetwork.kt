package com.example.admin.music.network

import com.example.admin.music.base.BasedNetwork
import com.example.admin.music.data.SongCheck
import com.example.admin.music.data.SongData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

object SongNetwork : BasedNetwork() {
    private val song: Song = retrofit.create(Song::class.java)
    fun getSongUrl(id: String): Call<SongData> = song.getSongUrl(id)
    fun accessible(id: String): Call<SongCheck> = song.accessible(id)
    interface Song {
        @GET("/song/url")
        fun getSongUrl(@Query("id") id: String): Call<SongData>

        @GET("/check/music")
        fun accessible(@Query("id") id: String): Call<SongCheck>
    }
}