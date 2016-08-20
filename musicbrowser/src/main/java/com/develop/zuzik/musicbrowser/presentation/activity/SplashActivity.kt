package com.develop.zuzik.musicbrowser.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.develop.zuzik.musicbrowser.presentation.fragment.splash.SplashFragment
import com.develop.zuzik.musicbrowser.presentation.fragment.splash.SplashFragmentContainer

class SplashActivity : FragmentContainerActivity(), SplashFragmentContainer {

    companion object {
        fun createIntent(context: Context) = Intent(context, SplashActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addFragment { SplashFragment.create() }
    }
}
