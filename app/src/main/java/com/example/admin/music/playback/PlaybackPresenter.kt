package com.example.admin.music.playback

import com.example.admin.music.data.PlaylistDetailBean
import com.example.admin.music.network.PlaylistNetwork
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaybackPresenter(val view: PlaybackContract.View) : PlaybackContract.Presenter {
    override fun getData(playlistId: String, position: Int) {
        PlaylistNetwork.getDetail(playlistId).enqueue(object : Callback<PlaylistDetailBean.PlaylistDetailData> {
            override fun onFailure(call: Call<PlaylistDetailBean.PlaylistDetailData>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(
                call: Call<PlaylistDetailBean.PlaylistDetailData>,
                response: Response<PlaylistDetailBean.PlaylistDetailData>
            ) {
                val tracksBean = response.body()?.playlist?.tracks?.get(position)!!
                view.apply {
                    initText(tracksBean.name, tracksBean.ar[0].name)
                    setBackground(tracksBean.al.picUrl)
                    initRotatePic(tracksBean.al.picUrl)
                    initSeekBar()
                }
            }
        })
    }
}