package com.develop.zuzik.musicbrowser.presentation.fragment.splash

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.develop.zuzik.musicbrowser.R
import com.develop.zuzik.musicbrowser.domain.exception.InterfaceNotImplementedException

class SplashFragment : Fragment() {

    companion object {
        fun create(): SplashFragment = SplashFragment()
    }

    private var container: SplashFragmentContainer? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_splash, container, false)
    }

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
}
