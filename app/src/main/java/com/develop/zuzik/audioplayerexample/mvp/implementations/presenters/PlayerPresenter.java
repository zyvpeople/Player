package com.develop.zuzik.audioplayerexample.mvp.implementations.presenters;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.PlaybackBundle;

import rx.Subscription;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerPresenter implements Player.Presenter {

	private final Player.Model model;
	private Player.View view;
	private Subscription playbackStateChangedSubscription;

	public PlayerPresenter(Player.Model model) {
		this.model = model;
	}

	@Override
	public void onInit(Player.View view) {
		this.view = view;
		this.model.init();
	}

	@Override
	public void onDestroy() {
		this.view = null;
		this.model.destroy();
	}

	@Override
	public void onAppear() {
		this.view.display(this.model.getPlaybackState());
		this.playbackStateChangedSubscription = this.model.onPlaybackStateChanged().subscribe(bundle -> this.view.display(bundle));
	}

	@Override
	public void onDisappear() {
		this.playbackStateChangedSubscription.unsubscribe();
	}

	@Override
	public PlaybackBundle getPlaybackState() {
		return this.model.getPlaybackState();
	}

	@Override
	public void onPlay(Context context) {
		this.model.play(context);
	}

	@Override
	public void onPause() {
		this.model.pause();
	}

	@Override
	public void onSeekToPosition(int positionInMilliseconds) {
		this.model.seekToPosition(positionInMilliseconds);
	}

	@Override
	public void onRepeat() {
		this.model.repeat();
	}

	@Override
	public void onDoNotRepeat() {
		this.model.doNotRepeat();
	}
}
