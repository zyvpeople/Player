package com.develop.zuzik.musicbrowser.presentation.player

import android.content.Context
import android.support.annotation.StringRes
import com.develop.zuzik.musicbrowser.R
import com.develop.zuzik.player.interfaces.PlayerExceptionMessageProvider

/**
 * User: zuzik
 * Date: 6/12/16
 */
class PlayerExceptionMessageProviderImpl(private val context: Context) : PlayerExceptionMessageProvider {

    override fun playerInitializeExceptionMessage(): String = getString(R.string.player_error_message_initialize)

    override fun failRequestAudioFocusExceptionMessage(): String = getString(R.string.player_error_message_request_audio_focus)

    override fun audioFocusLostExceptionMessage(): String = getString(R.string.player_error_message_audio_focus_lost)

    override fun mediaPlayerStateExceptionMessage(): String = getString(R.string.player_error_message_state)

    override fun fakeExceptionMessage(): String = getString(R.string.player_error_message_fake)

    override fun unknownExceptionMessage(): String = getString(R.string.player_error_message_unknown)

    private fun getString(@StringRes stringResId: Int) = this.context.getString(stringResId)

}
