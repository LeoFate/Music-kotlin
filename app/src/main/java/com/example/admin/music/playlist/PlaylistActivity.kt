package com.example.admin.music.playlist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.admin.music.R
import com.example.admin.music.base.BaseActivity
import com.example.admin.music.data.PlaylistDetailBean

class PlaylistActivity : BaseActivity(), PlaylistContact.View {
    private var id: String = ""
    private lateinit var recyclerView: RecyclerView
    private var playlistPresenter = PlaylistPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        layout = R.layout.activity_playlist
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        id = intent.extras!!.getString("id")!!
        recyclerView = findViewById(R.id.playlist_recyclerView)
        playlistPresenter.getData(id)
    }

    override fun initRV(playlistDetailData: PlaylistDetailBean.PlaylistDetailData) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PlaylistDetailAdapter(this, playlistDetailData)
    }
}
