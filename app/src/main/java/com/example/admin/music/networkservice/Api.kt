package com.example.admin.music.networkservice

import com.example.admin.music.data.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    interface Login {
        @GET("/login/cellphone")
        fun login(@Query("phone") phone: String, @Query("password") password: String): Call<LoginData>
    }

    interface Main {
        @GET("/user/Playlist")
        fun getPlayList(@Query("uid") uid: String): Call<PlaylistBean.PlaylistData>
    }

    interface Playlist {
        @GET("/Playlist/detail")
        fun getPlayListDetail(@Query("id") id: String): Call<PlaylistDetailBean.PlaylistDetailData>
    }

    interface Song {
        @GET("/song/url")
        fun getSongUrl(@Query("id") id: String): Call<SongData>

        @GET("/check/music")
        fun accessible(@Query("id") id: String): Call<SongCheck>
    }
}
