package com.develop.zuzik.musicbrowser.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.musicbrowser.R

class ScreensRoutingActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, ScreensRoutingActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screens_routing)
    }
}