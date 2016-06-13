package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayerModelState;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.MultiplePlayback;
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
	private final PublishSubject<Throwable> errorPlayingPublishSubject = PublishSubject.create();
	private boolean repeat;
	private boolean shuffle;

	public MultiplePlayerModel(MultiplePlayback playback) {
		this.playback = playback;
	}

	@Override
	public void init() {
		this.playback.init();
		this.playback.setListener(new MultiplePlaybackListener() {
			@Override
			public void onUpdate() {
				playbackStateChangedPublishSubject.onNext(null);
			}

			@Override
			public void onError(Throwable throwable) {
				errorPlayingPublishSubject.onNext(throwable);
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
		return new MultiplePlayerModelState(this.playback.getMultiplePlaybackState(), this.repeat, this.shuffle);
	}

	@Override
	public Observable<Void> stateChangedObservable() {
		return this.playbackStateChangedPublishSubject.asObservable();
	}

	@Override
	public Observable<Throwable> errorPlayingObservable() {
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
	public void repeatSingle() {
		this.repeat = true;
		this.playback.repeatSingle();
	}

	@Override
	public void doNotRepeatSingle() {
		this.repeat = false;
		this.playback.doNotRepeatSingle();
	}

	@Override
	public void shuffle() {
		this.shuffle = true;
		this.playback.shuffle();
	}

	@Override
	public void doNotShuffle() {
		this.shuffle = false;
		this.playback.doNotShuffle();
	}

	@Override
	public void simulateError() {
		this.playback.simulateError();
	}
}
