package com.example.admin.music.main

import com.example.admin.music.data.PlaylistBean

interface MainContact {
    interface View {
        fun getLoginMessage()
        fun initDrawer(avatar: String, nickName: String)
        fun initToolBar()
        fun initViewPager(uid: String)
    }

    interface Presenter

    interface MineFragment {
        fun initRv(playlistData: PlaylistBean.PlaylistData)
    }

    interface MinePresenter {
        fun getData(uid: String)
    }

    interface MineAdapter
}
