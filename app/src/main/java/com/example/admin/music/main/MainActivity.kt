package com.example.admin.music.main

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.admin.music.R
import com.example.admin.music.base.BaseActivity
import com.example.admin.music.base.getMyIntent
import com.example.admin.music.login.LoginActivity
import java.util.*

class MainActivity : BaseActivity(), MainContact.View {

    private lateinit var mDrawer: DrawerLayout
    private lateinit var mToolBar: Toolbar
    private lateinit var avatar: ImageView
    private lateinit var nickName: TextView
    private lateinit var logout: TextView
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var viewPager: ViewPager
    private var userName: String = ""
    private var id: String = ""
    private var mAvatar: String = ""
    private var mNickName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        layout = R.layout.activity_main
        super.onCreate(savedInstanceState)
    }

    override fun initView() {
        mDrawer = findViewById(R.id.main_drawer)
        mToolBar = findViewById(R.id.main_toolBar)
        avatar = findViewById(R.id.avatar)
        nickName = findViewById(R.id.nick_name)
        logout = findViewById(R.id.logout)
        viewPager = findViewById(R.id.main_viewPager)
        getLoginMessage()
        initToolBar()
        initDrawer(mAvatar, mNickName)
        initViewPager(id)
    }

    override fun getLoginMessage() {
        val bundle = intent.extras!!
        if (callClass == LoginActivity::class.java.name) {
            Toast.makeText(this, "Login successfully.", Toast.LENGTH_SHORT).show()
            userName = bundle.getString("userName")!!
            id = bundle.getString("id")!!
            mAvatar = bundle.getString("avatar")!!
            mNickName = bundle.getString("nickName")!!
        }
    }

    override fun initToolBar() {
        setSupportActionBar(mToolBar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun initDrawer(avatar: String, nickName: String) {
        Glide.with(this)
            .load(avatar)
            .into(this.avatar)
        this.nickName.text = nickName
        logout.setOnClickListener {
            startActivity(getMyIntent(this, LoginActivity::class.java))
            finish()
        }
        mToggle = ActionBarDrawerToggle(this, mDrawer, mToolBar, R.string.drawer_open, R.string.drawer_close)
        mToggle.drawerArrowDrawable = DrawerArrowDrawable(this)
        mDrawer.setDrawerListener(mToggle)

    }

    override fun initViewPager(uid: String) {
        val fragmentList = ArrayList<Fragment>()
        val mineFragment = MineFragment()
        val bundle = Bundle()
        bundle.putString("uid", uid)
        mineFragment.arguments = bundle
        fragmentList.add(mineFragment)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mToggle.onConfigurationChanged(newConfig)
    }
}
