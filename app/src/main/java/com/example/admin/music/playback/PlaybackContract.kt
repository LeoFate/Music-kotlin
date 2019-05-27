package com.example.admin.music.playback

interface PlaybackContract {
    interface View {
        fun initId()
        fun setBackground(url: String)
        fun initRotatePic(url: String)
        fun initText(songName: String, singer: String)
        fun initButton()
        fun initSeekBar()
        fun upgradeUI(position: Int)
    }

    interface Presenter {
        fun getData(playlistId: String, position: Int)
    }

    interface Service {
        fun pause()
        fun start()
        fun previous()
        fun next()
        fun getData(index: Int, playlistId: String = "")
    }
}