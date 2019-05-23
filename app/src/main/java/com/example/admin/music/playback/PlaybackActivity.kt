package com.example.admin.music.playback

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.admin.music.R
import com.example.admin.music.base.BaseActivity
import jp.wasabeef.glide.transformations.BlurTransformation

class PlaybackActivity : BaseActivity(), PlaybackContract.View {
    lateinit var leftArrow: ImageView
    lateinit var lastSong: ImageView
    lateinit var nextSong: ImageView
    lateinit var playBtn: ImageView
    lateinit var pauseBtn: ImageView
    lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        layout = R.layout.activity_playback
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        setBackground(intent.getStringExtra("picUrl"))
        leftArrow = findViewById(R.id.left_arrow)
        lastSong = findViewById(R.id.last_song)
        nextSong = findViewById(R.id.next_song)
        playBtn = findViewById(R.id.play_button)
        pauseBtn = findViewById(R.id.pause_button)
        seekBar = findViewById(R.id.seek_bar)
        initButton()
    }

    override fun setBackground(url: String) {
        val linearLayout = findViewById<LinearLayout>(R.id.playback_root_layout)
        Glide.with(this)
            .asDrawable()
            .load(url)
            .transform(MultiTransformation(BlurTransformation(30, 20)))
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    linearLayout.background = resource
                }
            })
    }

    override fun initButton() {
        val songService = SongService.instance
        pauseBtn.setOnClickListener {
            pauseBtn.visibility = View.GONE
            playBtn.visibility = View.VISIBLE
            songService?.pause()
        }
        playBtn.setOnClickListener {
            playBtn.visibility = View.GONE
            pauseBtn.visibility = View.VISIBLE
            songService?.start()
        }
        lastSong.setOnClickListener {
            songService?.previous()
        }
        nextSong.setOnClickListener {
            songService?.next()
        }
    }

    override fun initSeekBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}