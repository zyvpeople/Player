package com.develop.zuzik.musicbrowser.application

import android.app.Application
import com.develop.zuzik.musicbrowser.application.injection.component.ApplicationComponent
import com.develop.zuzik.musicbrowser.application.injection.component.DaggerApplicationComponent
import timber.log.Timber

/**
 * User: zuzik
 * Date: 8/20/16
 */
class App : Application() {

    companion object {
        lateinit var INSTANCE: App
            private set
    }

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        this.applicationComponent = DaggerApplicationComponent.create()
        Timber.plant(Timber.DebugTree())
    }
}