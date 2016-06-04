package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.RawRes;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.Playback;
import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerModel implements Player.Model {

	private final Playback playback;
	private final PublishSubject<PlaybackBundle> playbackStateChangedPublishSubject = PublishSubject.create();

	public PlayerModel(Uri uri) {
		this.playback = new Playback(uri);
	}

	public PlayerModel(@RawRes int rawResId) {
		this.playback = new Playback(rawResId);
	}

	@Override
	public void init() {
		this.playback.init();
		this.playback.setPlaybackListener(bundle -> {
			this.playbackStateChangedPublishSubject.onNext(bundle);
		});
	}

	@Override
	public void destroy() {
		this.playback.setPlaybackListener(null);
		this.playback.release();
	}

	@Override
	public PlaybackBundle getPlaybackState() {
		return this.playback.getPlaybackBundle();
	}

	@Override
	public Observable<PlaybackBundle> onPlaybackStateChanged() {
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
	public void seekToPosition(int positionInMilliseconds) {
		this.playback.seekTo(positionInMilliseconds);
	}

	@Override
	public void repeat() {
		this.playback.setRepeat(true);
	}

	@Override
	public void doNotRepeat() {
		this.playback.setRepeat(false);
	}
}
