package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceBroadcastIntentFactory;
import com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/4/16
 */
//FIXME:implement
public class PlayerServiceModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final Context context;
	private PlaybackState<SourceInfo> playbackState;

	public PlayerServiceModel(Context context, PlayerSource<SourceInfo> source) {
		this.context = new ContextWrapper(context).getApplicationContext();
//		this.playbackState = new PlaybackState<>(State.NONE, 0, Optional.absent(), this.repeat, source);
	}

	@Override
	public void setSource(PlayerSource<SourceInfo> source) {

	}

//	@Override
//	public void init() {
//		this.context.startService(PlaybackServiceIntentFactory.createPlayerSource(this.context, this.playbackState.playerSource));
//		LocalBroadcastManager
//				.getInstance(this.context)
//				.registerReceiver(
//						this.playbackStateReceiver,
//						new IntentFilter(PlaybackServiceBroadcastIntentFactory.ACTION_PLAYBACK_STATE));
//		LocalBroadcastManager
//				.getInstance(this.context)
//				.registerReceiver(
//						this.errorReceiver,
//						new IntentFilter(PlaybackServiceBroadcastIntentFactory.ACTION_ERROR));
//	}

	@Override
	public void clear() {
//		this.context.stopService(PlaybackService.createForDestroy(this.context));
		LocalBroadcastManager
				.getInstance(this.context)
				.unregisterReceiver(this.playbackStateReceiver);
		LocalBroadcastManager
				.getInstance(this.context)
				.unregisterReceiver(this.errorReceiver);
	}

	@Override
	public Optional<PlaybackState<SourceInfo>> getState() {
		return Optional.absent();
//		return new PlayerModelState<>(this.playbackState, this.repeat);
	}

	@Override
	public void addListener(Listener<SourceInfo> listener) {

	}

	@Override
	public void removeListener(Listener<SourceInfo> listener) {

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
//		this.repeat = true;
		this.context.startService(PlaybackServiceIntentFactory.createRepeat(this.context));
	}

	@Override
	public void doNotRepeat() {
//		this.repeat = false;
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
//						playbackStateChangedPublishSubject.onNext(null);
					});
		}
	};

	private final BroadcastReceiver errorReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
//			PlaybackServiceBroadcastIntentFactory
//					.parseError(intent, errorPlayingPublishSubject::onNext);
		}
	};
}
