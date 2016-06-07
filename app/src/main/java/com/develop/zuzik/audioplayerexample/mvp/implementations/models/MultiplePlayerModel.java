package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayerModelState;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.RepeatMode;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlaybackListener;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerModel implements MultiplePlayer.Model {

	private final MultiplePlayback playback;
	private final PublishSubject<Void> playbackStateChangedPublishSubject = PublishSubject.create();
	private final PublishSubject<Void> errorPlayingPublishSubject = PublishSubject.create();
	private RepeatMode repeat = RepeatMode.NONE;

	public MultiplePlayerModel(MultiplePlayback playback) {
		this.playback = playback;
	}

	@Override
	public void init() {
		this.playback.init();
		this.playback.setListener(new MultiplePlaybackListener() {
			@Override
			public void onChange(MultiplePlayerStateBundle bundle) {
				playbackStateChangedPublishSubject.onNext(null);
			}

			@Override
			public void onError() {
				errorPlayingPublishSubject.onNext(null);
			}
		});
	}

	@Override
	public void destroy() {
		this.playback.setListener(null);
		this.playback.release();
	}

	@Override
	public MultiplePlayerModelState getState() {
		return new MultiplePlayerModelState(this.playback.getMultiplePlaybackBundle(), this.repeat);
	}

	@Override
	public Observable<Void> stateChangedObservable() {
		return this.playbackStateChangedPublishSubject.asObservable();
	}

	@Override
	public Observable<Void> onErrorPlayingObservable() {
		return this.errorPlayingPublishSubject.asObservable();
	}

	@Override
	public void play() {
		this.playback.play();
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
	public void repeat(RepeatMode repeatMode) {
		this.playback.repeat(repeatMode);
		this.repeat = repeatMode;
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
