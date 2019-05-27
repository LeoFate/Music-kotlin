package com.example.admin.music.playlist

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.admin.music.R
import com.example.admin.music.base.BaseRVAdapter
import com.example.admin.music.base.getMyIntent
import com.example.admin.music.data.PlaylistDetailBean
import com.example.admin.music.playback.PlaybackActivity
import com.example.admin.music.playback.SongService

class PlaylistDetailAdapter(context: Context, private val playlistDetailData: PlaylistDetailBean.PlaylistDetailData) :
    BaseRVAdapter(context), PlaylistContact.PlaylistDetailAdapter {

    companion object {
        private const val HEAD_TYPE = 0
        private const val MAIN_TYPE = 1
    }

    private val playlistId = playlistDetailData.playlist.id
    private val tracksList: List<PlaylistDetailBean.Track> = playlistDetailData.playlist.tracks
    private var songPosition = -1
    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            HEAD_TYPE
        else
            MAIN_TYPE
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (i) {
            HEAD_TYPE -> viewHolder = HeadHolder(layoutInflater.inflate(R.layout.item_playlist_head, viewGroup, false))
            MAIN_TYPE -> viewHolder = MainHolder(layoutInflater.inflate(R.layout.item_playlist_main, viewGroup, false))
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        when (getItemViewType(i)) {
            HEAD_TYPE -> {
                val headHolder = viewHolder as HeadHolder
                val playlistBean = playlistDetailData.playlist
                headHolder.author.text = playlistBean.creator.nickname
                Glide.with(viewHolder.itemView)
                    .load(playlistBean.coverImgUrl)
                    .into(headHolder.avatar)
                headHolder.name.text = playlistBean.name
            }

            MAIN_TYPE -> {
                songPosition = SongService.instance?.songPosition ?: -1
                val mainHolder = viewHolder as MainHolder
                val tracksBean = tracksList[i - 1]
                mainHolder.serialNumber.text = i.toString()
                mainHolder.name.text = tracksBean.name
                val singer = tracksBean.ar[0].name + " - " + tracksBean.al.name
                mainHolder.author.text = singer
                var isPlayingThis = songPosition == i - 1 && SongService.instance?.playlistId.equals(playlistId)
                if (isPlayingThis) {
                    mainHolder.playing.visibility = View.VISIBLE
                    mainHolder.serialNumber.visibility = View.INVISIBLE
                }
                mainHolder.itemView.setOnClickListener {
                    showNotification(tracksBean)/*FIXME*/
                    songPosition = SongService.instance?.songPosition ?: -1
                    isPlayingThis = songPosition == i - 1 && SongService.instance?.playlistId.equals(playlistId)
                    if (songPosition == i - 1 && isPlayingThis) run {
                        val intent = getMyIntent(context, PlaybackActivity::class.java).apply {
                            putExtra("playlistId", playlistId)
                            putExtra("name", tracksBean.name)
                            putExtra("singer", tracksBean.ar[0].name)
                            putExtra("picUrl", tracksBean.al.picUrl)
                        }
                        context.startActivity(intent)
                    } else run {
                        mainHolder.playing.visibility = View.VISIBLE
                        mainHolder.serialNumber.visibility = View.INVISIBLE
                        val intent = getMyIntent(context, SongService::class.java).apply {
                            putExtra(SongService.CLICK_POSITION, i - 1)
                            putExtra(SongService.PLAYLIST_ID, playlistId)
                        }
                        context.startService(intent)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return tracksList.size + 1
    }

    override fun showNotification(tracksBean: PlaylistDetailBean.Track) {
        val intent = getMyIntent(context, PlaybackActivity::class.java).apply {
            putExtra("name", tracksBean.name)
            putExtra("singer", tracksBean.ar[0].name)
            putExtra("picUrl", tracksBean.al.picUrl)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val builder = NotificationCompat.Builder(context, context.getString(R.string.playback_channel))
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            val notificationId = 10
            notify(notificationId, builder.build())
        }
    }

    internal inner class HeadHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.head_avatar)
        val name: TextView = itemView.findViewById(R.id.head_name)
        val author: TextView = itemView.findViewById(R.id.head_author)
    }

    internal inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serialNumber: TextView = itemView.findViewById(R.id.serial_number)
        val playing: ImageView = itemView.findViewById(R.id.is_playing)
        val name: TextView = itemView.findViewById(R.id.song_name)
        val author: TextView = itemView.findViewById(R.id.song_author)
    }
}
