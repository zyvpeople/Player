package com.develop.zuzik.musicbrowser.presentation.fragment.splash

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.develop.zuzik.musicbrowser.R
import com.develop.zuzik.musicbrowser.application.injection.injector.SplashFragmentInjector
import com.develop.zuzik.musicbrowser.application.settings.SplashSettings
import com.develop.zuzik.musicbrowser.domain.exception.InterfaceNotImplementedException
import rx.Observable
import rx.Subscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashFragment : Fragment() {

    companion object {
        fun create(): SplashFragment = SplashFragment()
    }

    @Inject
    lateinit var settings: SplashSettings
    private var container: SplashFragmentContainer? = null
    private var subscription: Subscription? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SplashFragmentContainer) {
            this.container = context as SplashFragmentContainer?
        } else {
            throw InterfaceNotImplementedException()
        }
    }

    override fun onDetach() {
        this.container = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplashFragmentInjector().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onResume() {
        super.onResume()
        this.subscription = Observable
                .timer(this.settings.screenVisibleTimeInMilliseconds.toLong(), TimeUnit.MILLISECONDS)
                .subscribe { this.container?.onSplashFinished() }
    }

    override fun onPause() {
        this.subscription?.unsubscribe()
        super.onPause()
    }
}
