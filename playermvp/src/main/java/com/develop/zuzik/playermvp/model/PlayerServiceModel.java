package com.develop.zuzik.playermvp.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.develop.zuzik.player.exception.ServiceIsNotDeclaredInManifestException;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.PlaybackFactory;
import com.develop.zuzik.player.interfaces.PlaybackListener;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.PlayerNotificationFactory;
import com.develop.zuzik.player.interfaces.State;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.service.PlaybackService;
import com.develop.zuzik.player.service.PlaybackServiceInitializeBundle;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.playermvp.composite.CompositeListener;
import com.develop.zuzik.playermvp.interfaces.PlaybackSettings;
import com.develop.zuzik.playermvp.interfaces.Player;
import com.fernandocejas.arrow.functions.Function;
import com.fernandocejas.arrow.optional.Optional;

import org.jetbrains.annotations.Nullable;

import static com.develop.zuzik.player.service.PlaybackServiceIntentFactory.create;
import static com.develop.zuzik.player.service.PlaybackServiceIntentFactory.createDoNotRepeat;
import static com.develop.zuzik.player.service.PlaybackServiceIntentFactory.createForInit;
import static com.develop.zuzik.player.service.PlaybackServiceIntentFactory.createPause;
import static com.develop.zuzik.player.service.PlaybackServiceIntentFactory.createPlay;
import static com.develop.zuzik.player.service.PlaybackServiceIntentFactory.createRepeat;
import static com.develop.zuzik.player.service.PlaybackServiceIntentFactory.createSeekTo;
import static com.develop.zuzik.player.service.PlaybackServiceIntentFactory.createSimulateError;
import static com.develop.zuzik.player.service.PlaybackServiceIntentFactory.createStop;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerServiceModel<SourceInfo> implements Player.Model<SourceInfo> {

	private final Context context;
	private final PlaybackSettings playbackSettings;
	private final PlaybackFactory<SourceInfo> playbackFactory;
	private final CompositeListener<SourceInfo> compositeListener = new CompositeListener<>();
	private Optional<PlayerSource<SourceInfo>> source = Optional.absent();
	private Optional<PlaybackService> boundedService = Optional.absent();
	private final int notificationId;
	private final PlayerNotificationFactory<SourceInfo> playerNotificationFactory;

	public PlayerServiceModel(Context context,
							  final PlaybackSettings playbackSettings,
							  PlaybackFactory<SourceInfo> playbackFactory,
							  int notificationId,
							  PlayerNotificationFactory<SourceInfo> playerNotificationFactory) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackSettings = playbackSettings;
		this.playbackFactory = playbackFactory;
		this.notificationId = notificationId;
		this.playerNotificationFactory = playerNotificationFactory;
		Listener<SourceInfo> updateSettingsListener = new Listener<SourceInfo>() {
			@Override
			public void onUpdate(PlaybackState<SourceInfo> state) {
				if (state.repeat) {
					playbackSettings.repeat();
				} else {
					playbackSettings.doNotRepeat();
				}
			}

			@Override
			public void onError(Throwable error) {

			}
		};
		this.compositeListener.addListener(updateSettingsListener);
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
		return this.source.transform(new Function<PlayerSource<SourceInfo>, PlaybackState<SourceInfo>>() {
			@Nullable
			@Override
			public PlaybackState<SourceInfo> apply(PlayerSource<SourceInfo> source) {
				return new PlaybackState<>(State.NONE, 0, Optional.<Integer>absent(), PlayerServiceModel.this.playbackSettings.isRepeat(), source);
			}
		});
	}

	@Override
	public void videoViewSetter(ParamAction<VideoViewSetter> success) {
		if (this.boundedService.isPresent()) {
			this.boundedService.get().videoViewSetter(success);
		}
	}

	@Override
	public void addListener(Listener<SourceInfo> listener) {
		this.compositeListener.addListener(listener);
	}

	@Override
	public void removeListener(Listener<SourceInfo> listener) {
		this.compositeListener.removeListener(listener);
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
								this.playbackSettings.isRepeat(),
								this.notificationId,
								this.playerNotificationFactory)));
	}

	private void startService(Intent intent) {
		ComponentName expectedServiceName = new ComponentName(this.context, PlaybackService.class);
		ComponentName serviceName = this.context.startService(intent);
		if (!expectedServiceName.equals(serviceName)) {
			throw new ServiceIsNotDeclaredInManifestException(PlaybackService.class);
		}
	}

	private void notifyOnUpdate() {
		if (getState().isPresent()) {
			notifyOnUpdate(getState().get());
		}
	}

	private void notifyOnUpdate(PlaybackState playbackState) {
		this.compositeListener.onUpdate(playbackState);
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
					compositeListener.onError(throwable);
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
