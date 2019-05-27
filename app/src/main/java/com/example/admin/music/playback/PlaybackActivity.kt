package com.example.admin.music.playback

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.admin.music.R
import com.example.admin.music.base.BaseActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import java.util.*

class PlaybackActivity : BaseActivity(), PlaybackContract.View {
    private lateinit var leftArrow: ImageView
    private lateinit var lastSong: ImageView
    private lateinit var nextSong: ImageView
    private lateinit var playBtn: ImageView
    private lateinit var pauseBtn: ImageView
    private lateinit var record: ImageView
    private lateinit var recordAnimator: ObjectAnimator
    private lateinit var seekBar: SeekBar
    private lateinit var songName: TextView
    private lateinit var singer: TextView
    private val playbackPresenter = PlaybackPresenter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        layout = R.layout.activity_playback
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        initId()
        setBackground(intent.getStringExtra("picUrl"))
        leftArrow = findViewById(R.id.left_arrow)
        initText(intent.getStringExtra("name"), intent.getStringExtra("singer"))
        initRotatePic(intent.getStringExtra("picUrl"))
        initButton()
        initSeekBar()
    }

    override fun initId() {
        songName = findViewById(R.id.song_name)
        singer = findViewById(R.id.singer)
        record = findViewById(R.id.record)
        lastSong = findViewById(R.id.last_song)
        nextSong = findViewById(R.id.next_song)
        playBtn = findViewById(R.id.play_button)
        pauseBtn = findViewById(R.id.pause_button)
        seekBar = findViewById(R.id.seek_bar)
    }

    override fun initText(songName: String, singer: String) {
        this.songName.text = songName
        this.singer.text = singer
    }

    override fun setBackground(url: String) {
        val linearLayout = findViewById<LinearLayout>(R.id.playback_root_layout)
        Glide.with(this)
            .asDrawable()
            .load(url)
            .transform(MultiTransformation(BlurTransformation(30, 20)))
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    linearLayout.background = resource
                }
            })
    }

    override fun initRotatePic(url: String) {
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(record)
        recordAnimator = ObjectAnimator.ofFloat(record, "rotation", 0f, 360f).apply {
            duration = 10000
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            start()
        }
    }

    override fun initButton() {
        val songService = SongService.instance!!
        pauseBtn.setOnClickListener {
            pauseBtn.visibility = View.GONE
            playBtn.visibility = View.VISIBLE
            recordAnimator.pause()
            songService.pause()
        }
        playBtn.setOnClickListener {
            playBtn.visibility = View.GONE
            pauseBtn.visibility = View.VISIBLE
            recordAnimator.resume()
            songService.start()
        }
        lastSong.setOnClickListener {
            songService.previous()
            upgradeUI(songService.songPosition)
        }
        nextSong.setOnClickListener {
            songService.next()
            upgradeUI(songService.songPosition)
        }
    }

    override fun initSeekBar() {
        val songService = SongService.instance!!
        seekBar.max = songService.mediaPlayer.duration
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                seekBar.progress = songService.mediaPlayer.currentPosition
            }
        }, 0, 1000)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b)
                    songService.mediaPlayer.seekTo(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    override fun upgradeUI(position: Int) {
        playbackPresenter.getData(intent.getStringExtra("playlistId"), position)
    }
}