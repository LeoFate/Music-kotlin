package com.example.admin.music.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaSync
import android.os.AsyncTask
import android.os.IBinder

class SongService : Service(),
    MediaPlayer.OnPreparedListener,
    MediaSync.OnErrorListener,
    MediaPlayer.OnCompletionListener {
    companion object {
        const val CLICK_POSITION = "click_position"
        const val PLAYLIST_ID = "playlist id"
    }

    enum class State {
        IDLE, PLAYING, STOP
    }

    private lateinit var mediaPlayer: MediaPlayer
    lateinit var songList: MutableList<String>
    var tracksIdList = mutableListOf<String>()
    private var songPosition = -1
    private lateinit var playlistId: String
    private var state = State.IDLE
    fun playMusic(url: String) {
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener(this@SongService)
            setOnCompletionListener(this@SongService)
        }
    }

    override fun onPrepared(mp: MediaPlayer) {
        mp.start()
        state = State.PLAYING
    }

    override fun onError(sync: MediaSync, what: Int, extra: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (songPosition < songList.size - 1) {
            songPosition++
            mediaPlayer.reset()
        } else {
            songPosition = 0
            mediaPlayer.reset()
        }
        SongTask().execute(songPosition.toString())
    }

    override fun onCreate() {
        mediaPlayer = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        songPosition = intent.getIntExtra(CLICK_POSITION, -1)
        when {
            state == State.IDLE -> {
                playlistId = intent.getStringExtra(PLAYLIST_ID)
                SongTask().execute(intent.getStringExtra(PLAYLIST_ID), songPosition.toString())
            }
            intent.getStringExtra(PLAYLIST_ID) != playlistId -> {
                mediaPlayer.reset()
                playlistId = intent.getStringExtra(PLAYLIST_ID)
                tracksIdList.clear()
                SongTask().execute(intent.getStringExtra(PLAYLIST_ID), songPosition.toString())
            }
            else -> {
                mediaPlayer.reset()
                SongTask().execute(songPosition.toString())
            }
        }
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    inner class SongTask : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg params: String): Void? {
            val index: Int
            if (tracksIdList.isEmpty()) {
                PlaylistNetwork.instance.getDetail(params[0]).execute().body()?.playlist?.trackIds?.forEach {
                    tracksIdList.add(it.id)
                }
                songList = MutableList(tracksIdList.size) { "" }
                index = params[1].toInt()
            } else {
                index = params[0].toInt()
            }
            songList[index] =
                    (SongNetwork.instance.getSongUrl(tracksIdList[index]).execute().body()?.data?.get(0)?.url!!)
            playMusic(songList[index])
            return null
        }
    }
}
