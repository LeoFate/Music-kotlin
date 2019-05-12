package com.example.admin.music.playlist

import com.example.admin.music.data.PlaylistDetailBean

interface PlaylistContact {
    interface View {
        fun initRV(playlistDetailData: PlaylistDetailBean.PlaylistDetailData)
    }

    interface PlaylistDetailAdapter

    interface Presenter
}
