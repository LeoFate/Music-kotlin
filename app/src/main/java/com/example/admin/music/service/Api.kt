package com.example.admin.music.service


import com.example.admin.music.data.LoginBean
import com.example.admin.music.data.PlaylistBean
import com.example.admin.music.data.PlaylistDetailBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    interface Login {
        @GET("/login/cellphone")
        fun login(@Query("phone") phone: String, @Query("password") password: String): Call<LoginBean.LoginData>
    }

    interface Main {
        @GET("/user/Playlist")
        fun getPlayList(@Query("uid") uid: String): Call<PlaylistBean.PlaylistData>
    }

    interface Playlist {
        @GET("/Playlist/detail")
        fun getPlayListDetail(@Query("id") id: String): Call<PlaylistDetailBean.PlaylistDetailData>
    }
}
