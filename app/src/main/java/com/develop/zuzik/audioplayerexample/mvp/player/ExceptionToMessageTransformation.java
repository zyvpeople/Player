package com.develop.zuzik.audioplayerexample.mvp.player;

import com.develop.zuzik.playermvp.interfaces.PlayerExceptionMessageProvider;
import com.develop.zuzik.player.exception.AudioFocusLostException;
import com.develop.zuzik.player.exception.FailRequestAudioFocusException;
import com.develop.zuzik.player.exception.FakeMediaPlayerException;
import com.develop.zuzik.player.exception.MediaPlayerStateException;
import com.develop.zuzik.player.exception.PlayerInitializeException;
import com.develop.zuzik.audioplayerexample.player.Transformation;

/**
 * User: zuzik
 * Date: 6/12/16
 */
public class ExceptionToMessageTransformation implements Transformation<Throwable, String> {

	private final PlayerExceptionMessageProvider provider;

	public ExceptionToMessageTransformation(PlayerExceptionMessageProvider provider) {
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
