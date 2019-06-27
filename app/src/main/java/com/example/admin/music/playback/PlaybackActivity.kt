package com.example.admin.music.playback

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
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
    private lateinit var record: ImageView
    private lateinit var recordAnimator: ObjectAnimator
    private lateinit var currentPosition: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var duration: TextView
    private lateinit var songName: TextView
    private lateinit var singer: TextView
    private val playbackPresenter = PlaybackPresenter(this)
    private val songService = SongService.instance!!
    private var isPause = false
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
        initRotateAnimation()
        initSeekBar()
    }

    override fun initId() {
        songName = findViewById(R.id.song_name)
        singer = findViewById(R.id.singer)
        record = findViewById(R.id.record)
        currentPosition = findViewById(R.id.current_position)
        seekBar = findViewById(R.id.seek_bar)
        duration = findViewById(R.id.duration)
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
            .transform(MultiTransformation(BlurTransformation(30, 30)))
            .into(object : CustomTarget<Drawable>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    linearLayout.background = resource
                }
            })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initRotatePic(url: String) {
        Glide.with(this)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(record)
        var x1 = 0f
        record.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.rawX
                }

                MotionEvent.ACTION_UP -> {
                    val x2 = event.rawX
                    when {
                        x2 - x1 > 2 * record.width / 5 -> {
                            songService.previous()
                            upgradeUI(songService.songPosition)
                        }

                        x2 - x1 < -2 * record.width / 5 -> {
                            songService.next()
                            upgradeUI(songService.songPosition)
                        }

                        Math.abs(x2 - x1) < 10 -> {
                            if (isPause) {
                                recordAnimator.resume()
                                songService.start()
                            } else {
                                recordAnimator.pause()
                                songService.pause()
                            }
                            isPause = !isPause
                        }
                    }
                }
            }
            true
        }
    }

    override fun initRotateAnimation() {
        recordAnimator = ObjectAnimator.ofFloat(record, "rotation", 0f, 360f).apply {
            duration = 30000
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            start()
        }
    }

    override fun initSeekBar() {
        val songService = SongService.instance!!
        Log.e("debug", seekBar.max.toString())
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val duration = songService.mediaPlayer.duration
                seekBar.max = duration
                val durationText = (duration / 60000).toString() + ":" + ((duration / 1000) % 60).toString()
                val currentPosition = songService.mediaPlayer.currentPosition
                seekBar.progress = currentPosition
                val currentText =
                    (currentPosition / 60000).toString() + ":" + ((currentPosition / 1000) % 60).toString()
                runOnUiThread {
                    this@PlaybackActivity.currentPosition.text = currentText
                    this@PlaybackActivity.duration.text = durationText
                }
            }
        }, 0, 100)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    songService.mediaPlayer.seekTo(i)
                }
                if (i == seekBar.max) {
                    timer.cancel()
                    upgradeUI(SongService.instance?.songPosition!!)
                }
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