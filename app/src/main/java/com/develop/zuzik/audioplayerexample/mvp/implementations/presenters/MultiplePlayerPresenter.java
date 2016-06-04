package com.develop.zuzik.audioplayerexample.mvp.implementations.presenters;

import android.content.Context;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.MultiplePlayer;
import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackBundle;
import com.develop.zuzik.audioplayerexample.player.MultiplePlaybackRepeatMode;

import rx.Subscription;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class MultiplePlayerPresenter implements MultiplePlayer.Presenter {

	private final MultiplePlayer.Model model;
	private MultiplePlayer.View view;
	private Subscription playbackStateChangedSubscription;
	private MultiplePlaybackRepeatMode repeatMode;

	public MultiplePlayerPresenter(MultiplePlayer.Model model) {
		this.model = model;
	}

	@Override
	public void onInit(MultiplePlayer.View view) {
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
		updateView();
		this.playbackStateChangedSubscription = this.model.onPlaybackStateChanged().subscribe(this::updateView);
	}

	private void updateView() {
		updateView(this.model.getPlaybackState());
	}

	private void updateView(MultiplePlaybackBundle bundle) {
		this.view.display(bundle, this.repeatMode);
	}

	@Override
	public void onDisappear() {
		this.playbackStateChangedSubscription.unsubscribe();
	}

	@Override
	public MultiplePlaybackBundle getPlaybackState() {
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
	public void onStop() {
		this.model.stop();
	}

	@Override
	public void onSkipNext() {
		this.model.skipNext();
	}

	@Override
	public void onSkipPrevious() {
		this.model.skipPrevious();
	}

	@Override
	public void onSeekToPosition(int positionInMilliseconds) {
		this.model.seekToPosition(positionInMilliseconds);
	}

	@Override
	public void onRepeat(MultiplePlaybackRepeatMode repeatMode) {
		this.model.repeat(repeatMode);
	}

	@Override
	public void onShuffle() {
		this.model.shuffle();
	}

	@Override
	public void onDoNotShuffle() {
		this.model.doNotShuffle();
	}

	@Override
	public void simulateError() {
		this.model.simulateError();
	}
}
