package com.develop.zuzik.audioplayerexample.mvp.implementations.presenters;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerExceptionMessageProvider;
import com.develop.zuzik.audioplayerexample.player.exceptions.AudioFocusLostException;
import com.develop.zuzik.audioplayerexample.player.exceptions.FailRequestAudioFocusException;
import com.develop.zuzik.audioplayerexample.player.exceptions.FakeMediaPlayerException;
import com.develop.zuzik.audioplayerexample.player.exceptions.MediaPlayerStateException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.interfaces.Transformation;

/**
 * User: zuzik
 * Date: 6/12/16
 */
public class ExceptionToMessageTransformation implements Transformation<Throwable, String> {

	private final PlayerExceptionMessageProvider provider;

	ExceptionToMessageTransformation(PlayerExceptionMessageProvider provider) {
		this.provider = provider;
	}

	@Override
	public String transform(Throwable throwable) {
		if (throwable instanceof PlayerInitializeException) {
			return this.provider.playerInitializeExceptionMessage();
		} else if (throwable instanceof FailRequestAudioFocusException) {
			return this.provider.failRequestAudioFocusExceptionMessage();
		} else if (throwable instanceof AudioFocusLostException) {
			return this.provider.audioFocusLostExceptionMessage();
		} else if (throwable instanceof MediaPlayerStateException) {
			return this.provider.mediaPlayerStateExceptionMessage();
		} else if (throwable instanceof FakeMediaPlayerException) {
			return this.provider.fakeExceptionMessage();
		} else {
			return this.provider.unknownExceptionMessage();
		}
	}
}
