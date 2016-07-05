package com.develop.zuzik.audioplayerexample.mvp.implementations.models;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.services.PlaybackService;
import com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory;
import com.fernandocejas.arrow.optional.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerServiceModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final Context context;
	private final PlaybackSettings playbackSettings;
	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final List<Listener<SourceInfo>> listeners = new ArrayList();
	private Optional<Playback<SourceInfo>> playback = Optional.absent();
	private Optional<PlaybackService> boundedService = Optional.absent();

	public PlayerServiceModel(Context context, PlaybackSettings playbackSettings, PlaybackFactory<SourceInfo> playbackFactory) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackSettings = playbackSettings;
		this.playbackFactory = playbackFactory;
	}

	@Override
	public void setSource(PlayerSource<SourceInfo> source) {
		this.context.startService(PlaybackServiceIntentFactory.createForSetSource(this.context, source, this.playbackFactory));
		this.context.bindService(PlaybackServiceIntentFactory.create(this.context), this.serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void clear() {
		this.context.unbindService(this.serviceConnection);
		this.context.stopService(PlaybackServiceIntentFactory.create(this.context));
		this.playback = Optional.absent();
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
		//TODO: there can be a lot of time before service starts so we should return some data - absent is incorrect
		return Optional.absent();
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
		this.context.startService(PlaybackServiceIntentFactory.createRepeat(this.context));
	}

	@Override
	public void doNotRepeat() {
		this.context.startService(PlaybackServiceIntentFactory.createDoNotRepeat(this.context));
	}

	@Override
	public void simulateError() {
		this.context.startService(PlaybackServiceIntentFactory.createSimulateError(this.context));
	}

	private final ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			PlaybackService.PlaybackServiceBinder playbackServiceBinder = (PlaybackService.PlaybackServiceBinder) binder;
			boundedService = Optional.of(playbackServiceBinder.getService());
			boundedService.get().setPlaybackListener(new PlaybackListener() {
				@Override
				public void onUpdate(PlaybackState playbackState) {
					for (Listener<SourceInfo> listener : listeners) {
						listener.onUpdate(playbackState);
					}
				}

				@Override
				public void onError(Throwable throwable) {
					for (Listener<SourceInfo> listener : listeners) {
						listener.onError(throwable);
					}
				}
			});
			//TODO: notify listener
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			boundedService = Optional.absent();
			//TODO: notify listener
		}
	};

}
