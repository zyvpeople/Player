package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerModelState;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.player_initializer.PlayerInitializer;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerModel implements Player.Model {

	private final Playback playback;
	private final PublishSubject<Void> playbackStateChangedPublishSubject = PublishSubject.create();
	private final PublishSubject<Void> errorPlayingPublishSubject = PublishSubject.create();
	private boolean repeat;

	public PlayerModel(Context context, PlayerInitializer source) {
		this.playback = new Playback(context, source);
	}

	@Override
	public void init() {
		this.playback.init();
		this.playback.setPlaybackListener(new PlaybackListener() {
			@Override
			public void onChange() {
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
		this.playback.setPlaybackListener(null);
		this.playback.release();
	}

	@Override
	public PlayerModelState getState() {
		return new PlayerModelState(this.playback.getState(), this.repeat);
	}

	@Override
	public Observable<Void> stateChangedObservable() {
		return this.playbackStateChangedPublishSubject.asObservable();
	}

	@Override
	public Observable<Void> errorPlayingObservable() {
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
		this.playback.repeat();
		this.repeat = true;
	}

	@Override
	public void doNotRepeat() {
		this.playback.doNotRepeat();
		this.repeat = false;
	}

	@Override
	public void simulateError() {
		this.playback.simulateError();
	}
}
