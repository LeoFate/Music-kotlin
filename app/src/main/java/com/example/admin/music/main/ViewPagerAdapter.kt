package com.example.admin.music.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter internal constructor(fm: FragmentManager, private val fragmentList: List<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(i: Int): Fragment {
        return fragmentList[i]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
