package com.example.admin.music.playlist

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.admin.music.data.PlaylistDetailBean
import com.example.admin.music.networkservice.PlaylistNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class PlaylistPresenter(private val view: PlaylistContact.View) : PlaylistContact.Presenter {

    override fun getData(id: String) {
        PlaylistNetwork.getDetail(id).enqueue(object : Callback<PlaylistDetailBean.PlaylistDetailData> {
            override fun onResponse(
                call: Call<PlaylistDetailBean.PlaylistDetailData>,
                response: Response<PlaylistDetailBean.PlaylistDetailData>
            ) {
                try {
                    view.initRV(response.body()!!)
                } catch (e: NullPointerException) {
                    Toast.makeText(view as Activity, "Null response.", Toast.LENGTH_SHORT).show()
                    Log.e(javaClass.name, "Fail to get Playlist", e)
                }
            }

            override fun onFailure(call: Call<PlaylistDetailBean.PlaylistDetailData>, t: Throwable) {
                Toast.makeText(view as Activity, "Network Failure.", Toast.LENGTH_SHORT).show()
                Log.e(javaClass.name, "Fail to get Playlist", t)
            }
        })
    }
}
