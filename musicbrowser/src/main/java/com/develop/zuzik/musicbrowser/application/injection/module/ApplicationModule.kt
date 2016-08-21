package com.develop.zuzik.musicbrowser.application.injection.module

import android.content.Context
import com.develop.zuzik.musicbrowser.application.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * User: zuzik
 * Date: 8/20/16
 */
@Singleton
@Module
class ApplicationModule {
    @Provides
    fun provideContext(): Context = App.INSTANCE
}