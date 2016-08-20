package com.develop.zuzik.musicbrowser.presentation.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.develop.zuzik.musicbrowser.R

abstract class FragmentContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
    }

    protected fun addFragment(factory: () -> Fragment) {
        val contentResId = R.id.content
        if (supportFragmentManager.findFragmentById(contentResId) == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(contentResId, factory())
                    .commit()
        }
    }
}
