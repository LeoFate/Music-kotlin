package com.example.admin.music.main

import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast
import com.example.admin.music.data.PlaylistBean
import com.example.admin.music.network.MainNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MinePresenter(private val fragment: MainContact.MineFragment) : MainContact.MinePresenter {

    override fun getData(uid: String) {
        val frag = fragment as Fragment
        val context = frag.context
        MainNetwork.getPlayList(uid).enqueue(object : Callback<PlaylistBean.PlaylistData> {
            override fun onResponse(
                call: Call<PlaylistBean.PlaylistData>,
                response: Response<PlaylistBean.PlaylistData>
            ) {
                try {
                    fragment.initRv(response.body()!!)
                } catch (e: NullPointerException) {
                    Toast.makeText(context, "Null response.", Toast.LENGTH_SHORT).show()
                    Log.e(javaClass.name, "Fail to get Playlist", e)
                }
            }

            override fun onFailure(call: Call<PlaylistBean.PlaylistData>, t: Throwable) {
                Toast.makeText(context, "Network Failure.", Toast.LENGTH_SHORT).show()
                Log.e(javaClass.name, "Fail to get Playlist", t)
            }
        })
    }
}
