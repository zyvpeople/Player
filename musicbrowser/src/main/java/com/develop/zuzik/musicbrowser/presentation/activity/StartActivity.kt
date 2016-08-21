package com.develop.zuzik.musicbrowser.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.musicbrowser.application.request_code.ActivityRequestCode

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivityForResult(SplashActivity.createIntent(this), ActivityRequestCode.StartActivity.SPLASH_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ActivityRequestCode.StartActivity.SPLASH_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                startActivity(ScreensRoutingActivity.createIntent(this))
            }
            finish()
        }
    }
}
