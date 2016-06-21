package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerModelState;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.State;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceBroadcastIntentFactory;
import com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory;
import com.fernandocejas.arrow.optional.Optional;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerServiceModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final PublishSubject<Void> playbackStateChangedPublishSubject = PublishSubject.create();
	private final PublishSubject<Throwable> errorPlayingPublishSubject = PublishSubject.create();
	private boolean repeat;
	private final Context context;
	private PlaybackState<SourceInfo> playbackState;

	public PlayerServiceModel(Context context, PlayerSource<SourceInfo> source) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackState = new PlaybackState<>(State.NONE, 0, Optional.absent(), this.repeat, source);
	}

	@Override
	public void init() {
		this.context.startService(PlaybackServiceIntentFactory.createPlayerSource(this.context, this.playbackState.playerSource));
		LocalBroadcastManager
				.getInstance(this.context)
				.registerReceiver(
						this.playbackStateReceiver,
						new IntentFilter(PlaybackServiceBroadcastIntentFactory.ACTION_PLAYBACK_STATE));
		LocalBroadcastManager
				.getInstance(this.context)
				.registerReceiver(
						this.errorReceiver,
						new IntentFilter(PlaybackServiceBroadcastIntentFactory.ACTION_ERROR));
	}

	@Override
	public void destroy() {
//		this.context.stopService(PlaybackService.createForDestroy(this.context));
		LocalBroadcastManager
				.getInstance(this.context)
				.unregisterReceiver(this.playbackStateReceiver);
		LocalBroadcastManager
				.getInstance(this.context)
				.unregisterReceiver(this.errorReceiver);
	}

	@Override
	public PlayerModelState<SourceInfo> getState() {
		return new PlayerModelState<>(this.playbackState, this.repeat);
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
		this.context.startService(PlaybackServiceIntentFactory.createPlay(this.context));
	}

	@Override
	public void pause() {
		this.context.startService(PlaybackServiceIntentFactory.createPause(this.context));
	}

	@Override
	public void stop() {
		this.context.startService(PlaybackServiceIntentFactory.createStop(this.context));
	}

	@Override
	public void seekToPosition(int positionInMilliseconds) {
		this.context.startService(PlaybackServiceIntentFactory.createSeekTo(this.context, positionInMilliseconds));
	}

	@Override
	public void repeat() {
		this.repeat = true;
		this.context.startService(PlaybackServiceIntentFactory.createRepeat(this.context));
	}

	@Override
	public void doNotRepeat() {
		this.repeat = false;
		this.context.startService(PlaybackServiceIntentFactory.createDoNotRepeat(this.context));
	}

	@Override
	public void simulateError() {
		this.context.startService(PlaybackServiceIntentFactory.createSimulateError(this.context));
	}

	private final BroadcastReceiver playbackStateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			PlaybackServiceBroadcastIntentFactory
					.parsePlaybackState(intent, value -> {
						playbackState = value;
						playbackStateChangedPublishSubject.onNext(null);
					});
		}
	};

	private final BroadcastReceiver errorReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			PlaybackServiceBroadcastIntentFactory
					.parseError(intent, errorPlayingPublishSubject::onNext);
		}
	};
}
