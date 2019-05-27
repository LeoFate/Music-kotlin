package com.example.admin.music.playback

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaSync
import android.os.IBinder
import android.os.PowerManager
import com.example.admin.music.networkservice.PlaylistNetwork
import com.example.admin.music.networkservice.SongNetwork

class SongService : Service(),
    MediaPlayer.OnPreparedListener,
    MediaSync.OnErrorListener,
    MediaPlayer.OnCompletionListener, PlaybackContract.Service {

    companion object {
        const val CLICK_POSITION = "click_position"
        const val PLAYLIST_ID = "playlist id"
        var instance: SongService? = null
    }

    lateinit var mediaPlayer: MediaPlayer
        private set
    private lateinit var songList: MutableList<String>
    private var tracksIdList = mutableListOf<String>()
    var songPosition = -1
        private set
    var playlistId = ""
        private set
    private var isPaused = false
    private fun playMusic(url: String) {
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener(this@SongService)
            setOnCompletionListener(this@SongService)
        }
    }

    private fun playMusic(position: Int) {
        if (songList[position] == "") {
            getData(position)
        } else {
            playMusic(songList[position])
        }
    }

    override fun onCreate() {
        instance = this
        mediaPlayer = MediaPlayer()
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        songPosition = intent.getIntExtra(CLICK_POSITION, -1)
        when (playlistId) {
            "" -> {
                playlistId = intent.getStringExtra(PLAYLIST_ID)
                getData(songPosition, intent.getStringExtra(PLAYLIST_ID))
            }

            intent.getStringExtra(PLAYLIST_ID) -> {
                reset(mediaPlayer)
                playMusic(songPosition)
            }

            else -> {
                reset(mediaPlayer)
                tracksIdList.clear()
                playlistId = intent.getStringExtra(PLAYLIST_ID)
                getData(songPosition, intent.getStringExtra(PLAYLIST_ID))
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

    private fun reset(mp: MediaPlayer) {
        mp.reset()
    }

    override fun pause() {
        mediaPlayer.pause()
        isPaused = true
    }

    override fun start() {
        mediaPlayer.start()
        isPaused = false
    }

    override fun previous() {
        if (songPosition > 0) {
            songPosition--
            reset(mediaPlayer)
        } else {
            songPosition = songList.size - 1
            reset(mediaPlayer)
        }
        playMusic(songPosition)
    }

    override fun next() {
        if (songPosition < songList.size - 1) {
            songPosition++
            reset(mediaPlayer)
        } else {
            songPosition = 0
            reset(mediaPlayer)
        }
        playMusic(songPosition)
    }

    override fun getData(index: Int, playlistId: String) {
        Thread(Runnable {
            if (tracksIdList.isEmpty()) {
                PlaylistNetwork.getDetail(playlistId).execute().body()?.playlist?.trackIds?.forEach {
                    tracksIdList.add(it.id)
                }
                songList = MutableList(tracksIdList.size) { "" }
            }
            songList[index] =
                (SongNetwork.getSongUrl(tracksIdList[index]).execute().body()?.data?.get(0)?.url!!)
            playMusic(songList[index])
        }).start()
    }

    override fun onPrepared(mp: MediaPlayer) {
        start()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        next()
    }

    override fun onError(sync: MediaSync, what: Int, extra: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
