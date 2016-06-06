package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.Playback;
import com.develop.zuzik.audioplayerexample.player.PlayerStateBundle;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerModel implements Player.Model {

	private final Playback playback;
	private final PublishSubject<Void> playbackStateChangedPublishSubject = PublishSubject.create();

	public PlayerModel(PlayerSource source) {
		this.playback = new Playback(source);
	}

	@Override
	public void init() {
		this.playback.init();
		this.playback.setPlaybackListener(() -> this.playbackStateChangedPublishSubject.onNext(null));
	}

	@Override
	public void destroy() {
		this.playback.setPlaybackListener(null);
		this.playback.release();
	}

	@Override
	public PlayerStateBundle getPlayerStateBundle() {
		return this.playback.getPlayerStateBundle();
	}

	@Override
	public Observable<Void> onPlayerStateChangedObservable() {
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
	public void seekToPosition(int positionInMilliseconds) {
		this.playback.seekTo(positionInMilliseconds);
	}

	@Override
	public void repeat() {
		this.playback.repeat();
	}

	@Override
	public void doNotRepeat() {
		this.playback.doNotRepeat();
	}

	@Override
	public void simulateError() {
		this.playback.simulateError();
	}
}
