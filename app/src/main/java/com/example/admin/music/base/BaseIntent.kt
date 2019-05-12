package com.example.admin.music.base

import android.content.Context
import android.content.Intent

const val KEY = "ClassName"

fun getMyIntent(context: Context, cls: Class<*>): Intent {
    val intent = Intent(context, cls)
    intent.putExtra(KEY, context.javaClass.name)
    return intent
}

fun getCallClass(intent: Intent): String {
    return intent.getStringExtra(KEY)
}