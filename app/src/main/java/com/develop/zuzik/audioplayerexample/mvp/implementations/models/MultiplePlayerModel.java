package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.player.MultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackRepeatMode;
import com.develop.zuzik.audioplayerexample.player.MultiplePlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.interfaces.MultiplePlaybackListener;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerModel implements MultiplePlayer.Model {

	private final MultiplePlayback playback;
	private final PublishSubject<MultiplePlayerStateBundle> playbackStateChangedPublishSubject = PublishSubject.create();
	private final PublishSubject<Void> errorPlayingPublishSubject = PublishSubject.create();

	public MultiplePlayerModel(MultiplePlayback playback) {
		this.playback = playback;
	}

	@Override
	public void init(Context context) {
		this.playback.init(context);
		this.playback.setListener(new MultiplePlaybackListener() {
			@Override
			public void onChange(MultiplePlayerStateBundle bundle) {
				playbackStateChangedPublishSubject.onNext(bundle);
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
	public MultiplePlayerStateBundle getPlaybackState() {
		return this.playback.getMultiplePlaybackBundle();
	}

	@Override
	public Observable<MultiplePlayerStateBundle> onPlaybackStateChangedObservable() {
		return this.playbackStateChangedPublishSubject.asObservable();
	}

	@Override
	public Observable<Void> onErrorPlayingObservable() {
		return this.errorPlayingPublishSubject.asObservable();
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
	public void skipNext(Context context) {
		this.playback.skipNext(context);
	}

	@Override
	public void skipPrevious(Context context) {
		this.playback.skipPrevious(context);
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
