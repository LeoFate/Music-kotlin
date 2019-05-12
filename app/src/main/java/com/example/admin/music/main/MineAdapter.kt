package com.example.admin.music.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.admin.music.playlist.PlaylistActivity
import com.example.admin.music.R
import com.example.admin.music.base.BaseRVAdapter
import com.example.admin.music.base.getMyIntent
import com.example.admin.music.data.PlaylistBean

class MineAdapter internal constructor(context: Context, playlistData: PlaylistBean.PlaylistData) :
    BaseRVAdapter(context), MainContact.MineAdapter {
    companion object {
        private const val CREATED_NAME_TYPE = 1
        private const val SUBSCRIBED_NAME_TYPE = 2
        private const val MAIN_TYPE = 3
    }

    private var createdPlaylistCount: Int = 0
    private var subscribedPlayListCount: Int = 0
    private val playlistList: List<PlaylistBean.Playlist>

    init {
        createdPlaylistCount = 0
        subscribedPlayListCount = 0
        playlistList = playlistData.playlist
        for (p in playlistList) {
            if (p.ordered) {
                subscribedPlayListCount++
            } else {
                createdPlaylistCount++
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> CREATED_NAME_TYPE
            createdPlaylistCount + 1 -> SUBSCRIBED_NAME_TYPE
            else -> MAIN_TYPE
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            CREATED_NAME_TYPE -> viewHolder =
                    NameHolder(layoutInflater.inflate(R.layout.item_mine_name, viewGroup, false))
            MAIN_TYPE -> viewHolder = MainHolder(layoutInflater.inflate(R.layout.item_mine_main, viewGroup, false))
            SUBSCRIBED_NAME_TYPE -> viewHolder =
                    NameHolder(layoutInflater.inflate(R.layout.item_mine_name, viewGroup, false))
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CREATED_NAME_TYPE -> {
                val nameHolder = viewHolder as NameHolder
                nameHolder.textView.text = "创建的歌单"
            }
            MAIN_TYPE -> {
                val mainHolder = viewHolder as MainHolder
                val playlistBean: PlaylistBean.Playlist
                val count: String
                if (position <= createdPlaylistCount) {
                    playlistBean = playlistList[position - 1]
                    count = playlistBean.trackCount.toString() + " 首"
                } else {
                    playlistBean = playlistList[position - 2]
                    count = playlistBean.trackCount.toString() + " 首 by " + playlistBean.creator.nickname
                }
                Glide.with(context)
                    .load(playlistBean.coverImgUrl)
                    .into(mainHolder.coverImage)
                var name = playlistBean.name
                if (position == 1) {
                    name = name.replace(name.substring(0, name.length - 5), "我")
                }
                mainHolder.title.text = name
                mainHolder.count.text = count
                mainHolder.itemView.setOnClickListener { v ->
                    val intent = getMyIntent(context, PlaylistActivity::class.java)
                    intent.putExtra("id", playlistBean.id)
                    context.startActivity(intent)
                }
            }
            SUBSCRIBED_NAME_TYPE -> {
                val nameHolder = viewHolder as NameHolder
                nameHolder.textView.text = "收藏的歌单"
            }
        }
    }

    override fun getItemCount(): Int {
        return createdPlaylistCount + subscribedPlayListCount + 2
    }

    internal inner class NameHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.item_mine_name)

    }

    internal inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var coverImage: ImageView = itemView.findViewById(R.id.playlist_image)
        var title: TextView = itemView.findViewById(R.id.playlist_name)
        var count: TextView = itemView.findViewById(R.id.playlist_count)
    }
}
