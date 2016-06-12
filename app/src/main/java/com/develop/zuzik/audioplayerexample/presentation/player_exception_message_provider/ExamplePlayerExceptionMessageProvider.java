package com.develop.zuzik.audioplayerexample.presentation.player_exception_message_provider;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerExceptionMessageProvider;

/**
 * User: zuzik
 * Date: 6/12/16
 */
public class ExamplePlayerExceptionMessageProvider implements PlayerExceptionMessageProvider {

	@Override
	public String playerInitializeExceptionMessage() {
		return "Error initialize player";
	}

	@Override
	public String failRequestAudioFocusExceptionMessage() {
		return "Error request audio focus";
	}

	@Override
	public String audioFocusLostExceptionMessage() {
		return "Audio focus lost error";
	}

	@Override
	public String mediaPlayerStateExceptionMessage() {
		return "Error usage media player";
	}

	@Override
	public String fakeExceptionMessage() {
		return "Fake error";
	}

	@Override
	public String unknownExceptionMessage() {
		return "Unknown error";
	}
}
