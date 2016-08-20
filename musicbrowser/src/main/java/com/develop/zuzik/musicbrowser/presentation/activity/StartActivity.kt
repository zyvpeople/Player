package com.develop.zuzik.musicbrowser.presentation.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(SplashActivity.createIntent(this))
        finish()
    }
}
