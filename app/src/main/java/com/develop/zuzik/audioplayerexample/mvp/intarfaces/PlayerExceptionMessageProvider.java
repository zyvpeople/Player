package com.develop.zuzik.audioplayerexample.mvp.intarfaces;

/**
 * User: zuzik
 * Date: 6/12/16
 */
public interface PlayerExceptionMessageProvider {

	String playerInitializeExceptionMessage();

	String failRequestAudioFocusExceptionMessage();

	String audioFocusLostExceptionMessage();

	String mediaPlayerStateExceptionMessage();

	String fakeExceptionMessage();

	String unknownExceptionMessage();
}
