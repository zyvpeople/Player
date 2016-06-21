package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerModelState;
import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final Playback<SourceInfo> playback;
	private final PublishSubject<Void> playbackStateChangedPublishSubject = PublishSubject.create();
	private final PublishSubject<Throwable> errorPlayingPublishSubject = PublishSubject.create();
	private boolean repeat;

	public PlayerModel(Context context, PlayerSource<SourceInfo> source) {
		this.playback = new Playback<>(context, source);
	}

	@Override
	public void init() {
		this.playback.init();
		this.playback.setPlaybackListener(new PlaybackListener() {
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
		this.playback.setPlaybackListener(null);
		this.playback.release();
	}

	@Override
	public PlayerModelState<SourceInfo> getState() {
		return new PlayerModelState<>(this.playback.getPlaybackState(), this.repeat);
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
	public void seekToPosition(int positionInMilliseconds) {
		this.playback.seekTo(positionInMilliseconds);
	}

	@Override
	public void repeat() {
		this.repeat = true;
		this.playback.repeat();
	}

	@Override
	public void doNotRepeat() {
		this.repeat = false;
		this.playback.doNotRepeat();
	}

	@Override
	public void simulateError() {
		this.playback.simulateError();
	}
}
