package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.notification.NotificationFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.State;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.services.PlaybackService;
import com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceInitializeBundle;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.create;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createDoNotRepeat;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createForInit;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createPause;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createPlay;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createRepeat;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createSeekTo;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createSimulateError;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createStop;

/**
 * User: zuzik
 * Date: 6/4/16
 */
@SuppressWarnings("Convert2Diamond")
public class PlayerServiceModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final Context context;
	private final PlaybackSettings playbackSettings;
	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final List<Listener<SourceInfo>> listeners = new ArrayList<>();
	private Optional<PlayerSource<SourceInfo>> source = Optional.absent();
	private Optional<PlaybackService> boundedService = Optional.absent();
	private final int notificationId;
	private final NotificationFactory<SourceInfo> notificationFactory;

	public PlayerServiceModel(Context context,
							  PlaybackSettings playbackSettings,
							  PlaybackFactory<SourceInfo> playbackFactory,
							  int notificationId,
							  NotificationFactory<SourceInfo> notificationFactory) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackSettings = playbackSettings;
		this.playbackFactory = playbackFactory;
		this.notificationId = notificationId;
		this.notificationFactory = notificationFactory;
	}

	@Override
	public void setSource(PlayerSource<SourceInfo> source) {
		this.source = Optional.of(source);
		startServiceForInit(source);
		this.context.bindService(create(this.context), this.serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void clear() {
		this.context.unbindService(this.serviceConnection);
		this.context.stopService(create(this.context));
		this.source = Optional.absent();
	}

	@Override
	public Optional<PlaybackState<SourceInfo>> getState() {
		if (this.boundedService.isPresent()) {
			Optional<PlaybackState> stateOptional = this.boundedService.get().getPlaybackState();
			if (stateOptional.isPresent()) {
				PlaybackState<SourceInfo> state = (PlaybackState<SourceInfo>) stateOptional.get();
				return Optional.of(state);
			}
		}
		return this.source.transform(source -> new PlaybackState<>(State.NONE, 0, Optional.absent(), this.playbackSettings.isRepeat(), source));
	}

	@Override
	public void addListener(Listener<SourceInfo> listener) {
		if (!this.listeners.contains(listener)) {
			this.listeners.add(listener);
		}
	}

	@Override
	public void removeListener(Listener<SourceInfo> listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void play() {
		startService(createPlay(this.context));
	}

	@Override
	public void pause() {
		startService(createPause(this.context));
	}

	@Override
	public void stop() {
		startService(createStop(this.context));
	}

	@Override
	public void seekToPosition(int positionInMilliseconds) {
		startService(createSeekTo(this.context, positionInMilliseconds));
	}

	@Override
	public void repeat() {
		startService(createRepeat(this.context));
	}

	@Override
	public void doNotRepeat() {
		startService(createDoNotRepeat(this.context));
	}

	@Override
	public void simulateError() {
		startService(createSimulateError(this.context));
	}

	private void startServiceForInit(PlayerSource<SourceInfo> source) {
		startService(
				createForInit(
						this.context,
						new PlaybackServiceInitializeBundle<>(
								source,
								this.playbackFactory,
								this.playbackSettings,
								this.notificationId,
								this.notificationFactory)));
	}

	private void startService(Intent play) {
		this.context.startService(play);
	}

	private void notifyOnUpdate() {
		if (getState().isPresent()) {
			notifyOnUpdate(getState().get());
		}
	}

	private void notifyOnUpdate(PlaybackState playbackState) {
		for (Listener<SourceInfo> listener : this.listeners) {
			listener.onUpdate(playbackState);
		}
	}

	private final ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			PlaybackService.PlaybackServiceBinder playbackServiceBinder = (PlaybackService.PlaybackServiceBinder) binder;
			boundedService = Optional.of(playbackServiceBinder.getService());
			boundedService.get().setPlaybackListener(new PlaybackListener() {
				@Override
				public void onUpdate(PlaybackState playbackState) {
					notifyOnUpdate(playbackState);
				}

				@Override
				public void onError(Throwable throwable) {
					for (Listener<SourceInfo> listener : listeners) {
						listener.onError(throwable);
					}
				}
			});
			notifyOnUpdate();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			boundedService = Optional.absent();
			notifyOnUpdate();
		}
	};
}
