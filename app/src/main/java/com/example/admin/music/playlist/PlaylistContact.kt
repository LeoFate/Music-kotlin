package com.example.admin.music.playlist

import com.example.admin.music.data.PlaylistDetailBean

interface PlaylistContact {
    interface View {
        fun initRV(playlistDetailData: PlaylistDetailBean.PlaylistDetailData)
        fun displaySpeaker(position: Int)
        fun disappearSpeaker(position: Int)
    }

    interface PlaylistDetailAdapter {
        //FIXME Notification do not work
        fun showNotification(tracksBean: PlaylistDetailBean.Track)
    }

    interface Presenter {
        fun getData(id: String)
    }
}
