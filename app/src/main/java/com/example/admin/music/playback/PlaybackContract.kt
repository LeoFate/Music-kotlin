package com.example.admin.music.playback

interface PlaybackContract {
    interface View {
        fun setBackground(url: String)
        fun initButton()
        fun initSeekBar()
    }
}