package com.develop.zuzik.multipleplayermvp.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackFactory;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayerNotificationFactory;
import com.develop.zuzik.multipleplayer.service.MultiplePlaybackService;
import com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceInitializeBundle;
import com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory;
import com.develop.zuzik.multipleplayermvp.composite.CompositeListener;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlaybackSettings;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.player.exception.ServiceIsNotDeclaredInManifestException;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.functions.Function;
import com.fernandocejas.arrow.optional.Optional;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.create;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createDoNotRepeatSingle;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createDoNotShuffle;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createPause;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createPlay;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createPlayNext;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createPlayPrevious;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createRepeatSingle;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createSeekTo;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createShuffle;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createSimulateError;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createStop;
import static com.develop.zuzik.multipleplayer.service.MultiplePlaybackServiceIntentFactory.createSwitchToSource;

/**
 * User: zuzik
 * Date: 7/14/16
 */
public class MultiplePlayerServiceModel<SourceInfo> implements MultiplePlayer.Model<SourceInfo> {

	private final Context context;
	private final MultiplePlaybackSettings playbackSettings;
	private final MultiplePlaybackFactory<SourceInfo> playbackFactory;
	private final CompositeListener<SourceInfo> compositeListener = new CompositeListener<>();
	private Optional<List<PlayerSource<SourceInfo>>> sources = Optional.absent();
	private Optional<MultiplePlaybackService> boundedService = Optional.absent();
	private final int notificationId;
	private final MultiplePlayerNotificationFactory<SourceInfo> playerNotificationFactory;

	public MultiplePlayerServiceModel(
			Context context,
			final MultiplePlaybackSettings playbackSettings,
			MultiplePlaybackFactory<SourceInfo> playbackFactory,
			int notificationId,
			MultiplePlayerNotificationFactory<SourceInfo> playerNotificationFactory) {
		this.context = new ContextWrapper(context).getApplicationContext();
		this.playbackSettings = playbackSettings;
		this.playbackFactory = playbackFactory;
		this.notificationId = notificationId;
		this.playerNotificationFactory = playerNotificationFactory;
		Listener<SourceInfo> updateSettingsListener = new Listener<SourceInfo>() {
			@Override
			public void onUpdate(MultiplePlaybackState<SourceInfo> state) {
				if (state.repeatSingle) {
					playbackSettings.repeatSingle();
				} else {
					playbackSettings.doNotRepeatSingle();
				}
				if (state.shuffle) {
					playbackSettings.shuffle();
				} else {
					playbackSettings.doNotShuffle();
				}
			}

			@Override
			public void onError(Throwable error) {
			}
		};
		this.compositeListener.addListener(updateSettingsListener);
	}

	@Override
	public void setSources(List<PlayerSource<SourceInfo>> playerSources) {
		this.sources = Optional.of(playerSources);
		startServiceForInit(playerSources);
		this.context.bindService(create(this.context), this.serviceConnection, Context.BIND_AUTO_CREATE);

	}

	@Override
	public void clear() {
		this.context.unbindService(this.serviceConnection);
		this.context.stopService(create(this.context));
		this.sources = Optional.absent();
	}

	@Override
	public Optional<MultiplePlaybackState<SourceInfo>> getState() {
		if (this.boundedService.isPresent()) {
			Optional<MultiplePlaybackState> stateOptional = this.boundedService.get().getMultiplePlaybackState();
			if (stateOptional.isPresent()) {
				MultiplePlaybackState<SourceInfo> state = (MultiplePlaybackState<SourceInfo>) stateOptional.get();
				return Optional.of(state);
			}
		}
		return this.sources.transform(new Function<List<PlayerSource<SourceInfo>>, MultiplePlaybackState<SourceInfo>>() {
			@Nullable
			@Override
			public MultiplePlaybackState<SourceInfo> apply(List<PlayerSource<SourceInfo>> input) {
				return new MultiplePlaybackState<>(input, Optional.<PlaybackState<SourceInfo>>absent(), MultiplePlayerServiceModel.this.playbackSettings.isRepeatSingle(), MultiplePlayerServiceModel.this.playbackSettings.isShuffle());
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
	public void skipNext() {
		startService(createPlayNext(this.context));
	}

	@Override
	public void skipPrevious() {
		startService(createPlayPrevious(this.context));
	}

	@Override
	public void seekToPosition(int positionInMilliseconds) {
		startService(createSeekTo(this.context, positionInMilliseconds));
	}

	@Override
	public void repeatSingle() {
		startService(createRepeatSingle(this.context));
	}

	@Override
	public void doNotRepeatSingle() {
		startService(createDoNotRepeatSingle(this.context));
	}

	@Override
	public void shuffle() {
		startService(createShuffle(this.context));
	}

	@Override
	public void doNotShuffle() {
		startService(createDoNotShuffle(this.context));
	}

	@Override
	public void switchToSource(PlayerSource<SourceInfo> source) {
		startService(createSwitchToSource(this.context, source));
	}

	@Override
	public void simulateError() {
		startService(createSimulateError(this.context));
	}

	private void startServiceForInit(List<PlayerSource<SourceInfo>> sources) {
		startService(
				MultiplePlaybackServiceIntentFactory.createForInit(
						this.context,
						new MultiplePlaybackServiceInitializeBundle<>(
								this.playbackFactory,
								sources,
								this.notificationId,
								this.playerNotificationFactory)));
	}

	private void startService(Intent intent) {
		ComponentName expectedServiceName = new ComponentName(this.context, MultiplePlaybackService.class);
		ComponentName serviceName = this.context.startService(intent);
		if (!expectedServiceName.equals(serviceName)) {
			throw new ServiceIsNotDeclaredInManifestException(MultiplePlaybackService.class);
		}
	}

	private void notifyOnUpdate() {
		if (getState().isPresent()) {
			notifyOnUpdate(getState().get());
		}
	}

	private void notifyOnUpdate(MultiplePlaybackState<SourceInfo> playbackState) {
		this.compositeListener.onUpdate(playbackState);
	}

	private final ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			MultiplePlaybackService.MultiplePlaybackServiceBinder playbackServiceBinder = (MultiplePlaybackService.MultiplePlaybackServiceBinder) binder;
			boundedService = Optional.of(playbackServiceBinder.getService());
			boundedService.get().setMultiplePlaybackListener(new MultiplePlaybackListener() {
				@Override
				public void onUpdate(MultiplePlaybackState multiplePlaybackState) {
					notifyOnUpdate(multiplePlaybackState);
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
