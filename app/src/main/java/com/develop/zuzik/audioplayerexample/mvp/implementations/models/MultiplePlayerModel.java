package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.player.MultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackRepeatMode;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerModel implements MultiplePlayer.Model {

	private final MultiplePlayback playback;
	private final PublishSubject<MultiplePlaybackBundle> playbackStateChangedPublishSubject = PublishSubject.create();

	public MultiplePlayerModel(MultiplePlayback playback) {
		this.playback = playback;
	}

	@Override
	public void init() {
		this.playback.init();
		this.playback.setListener(bundle -> this.playbackStateChangedPublishSubject.onNext(bundle));
	}

	@Override
	public void destroy() {
		this.playback.setListener(null);
		this.playback.release();
	}

	@Override
	public MultiplePlaybackBundle getPlaybackState() {
		return this.playback.getMultiplePlaybackBundle();
	}

	@Override
	public Observable<MultiplePlaybackBundle> onPlaybackStateChanged() {
		return this.playbackStateChangedPublishSubject.asObservable();
	}

	@Override
	public void play(Context context) {
		this.playback.play(context);
	}

	@Override
	public void pause() {
		this.playback.pause();
	}

	@Override
	public void stop() {
		this.playback.stop();
	}

	@Override
	public void skipNext() {
		this.playback.skipNext();
	}

	@Override
	public void skipPrevious() {
		this.playback.skipPrevious();
	}

	@Override
	public void seekToPosition(int positionInMilliseconds) {
		this.playback.seekTo(positionInMilliseconds);
	}

	@Override
	public void repeat(MultiplePlaybackRepeatMode repeatMode) {
		this.playback.repeat(repeatMode);
	}

	@Override
	public void shuffle() {
		this.playback.shuffle();
	}

	@Override
	public void doNotShuffle() {
		this.playback.doNotShuffle();
	}

	@Override
	public void simulateError() {
		this.playback.simulateError();
	}
}
