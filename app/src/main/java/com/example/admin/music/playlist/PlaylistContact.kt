package com.example.admin.music.playlist

import com.example.admin.music.data.PlaylistDetailBean

interface PlaylistContact {
    interface View {
        fun initRV(playlistDetailData: PlaylistDetailBean.PlaylistDetailData)
    }

    interface PlaylistDetailAdapter{
        fun getSongUrl(urlList: List<String>)
    }
    interface Presenter {
        fun getData(id: String)
    }
}
