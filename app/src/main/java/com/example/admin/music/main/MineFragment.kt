package com.example.admin.music.main

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.admin.music.R
import com.example.admin.music.base.BaseFragment
import com.example.admin.music.data.PlaylistBean

class MineFragment : BaseFragment(), MainContact.MineFragment {
    private lateinit var recyclerView: RecyclerView
    private var presenter = MinePresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = R.layout.fragment_mine
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.mine_RecyclerView)
        presenter.getData(arguments!!.getString("uid")!!)
    }

    override fun initRv(playlistData: PlaylistBean.PlaylistData) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MineAdapter(context as Context, playlistData)
    }
}
