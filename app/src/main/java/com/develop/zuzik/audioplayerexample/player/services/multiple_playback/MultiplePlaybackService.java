package com.develop.zuzik.audioplayerexample.player.services.multiple_playback;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlayback;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.interfaces.MultiplePlaybackState;
import com.develop.zuzik.audioplayerexample.player.multiple_playback.null_objects.NullMultiplePlaybackListener;
import com.develop.zuzik.audioplayerexample.player.notification.MultiplePlayerNotificationFactory;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import java.util.List;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class MultiplePlaybackService extends Service {

	private static final int PENDING_INTENT_ID_PLAY = 1;
	private static final int PENDING_INTENT_ID_PAUSE = 2;
	private static final int PENDING_INTENT_ID_STOP = 3;
	private static final int PENDING_INTENT_ID_PLAY_NEXT = 4;
	private static final int PENDING_INTENT_ID_PLAY_PREVIOUS = 5;

	private final IBinder binder = new MultiplePlaybackServiceBinder();
	private Optional<MultiplePlayback> multiplePlayback = Optional.absent();
	private MultiplePlaybackListener multiplePlaybackListener = NullMultiplePlaybackListener.getInstance();
	private int notificationId;
	private MultiplePlayerNotificationFactory multiplePlayerNotificationFactory;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(getClass().getSimpleName(), "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(getClass().getSimpleName(), "onStartCommand");
		MultiplePlaybackServiceIntentFactory.parseForInit(intent, bundle -> {
			MultiplePlaybackFactory multiplePlaybackFactory = bundle.multiplePlaybackFactory;
			List<PlayerSource> playerSources = bundle.playerSources;
			this.notificationId = bundle.notificationId;
			this.multiplePlayerNotificationFactory = bundle.multiplePlayerNotificationFactory;
			if (this.multiplePlayback.isPresent()) {
				this.multiplePlayback.get().setPlayerSources(playerSources);
			} else {
				initMultiplePlayback(multiplePlaybackFactory, playerSources);
			}
		});
		MultiplePlaybackServiceIntentFactory.parsePlay(intent, () -> getMultiplePlayback(MultiplePlayback::play));
		MultiplePlaybackServiceIntentFactory.parsePause(intent, () -> getMultiplePlayback(MultiplePlayback::pause));
		MultiplePlaybackServiceIntentFactory.parseStop(intent, () -> getMultiplePlayback(MultiplePlayback::stop));
		MultiplePlaybackServiceIntentFactory.parsePlayNext(intent, () -> getMultiplePlayback(MultiplePlayback::playNextPlayerSource));
		MultiplePlaybackServiceIntentFactory.parsePlayPrevious(intent, () -> getMultiplePlayback(MultiplePlayback::playPreviousPlayerSource));
		MultiplePlaybackServiceIntentFactory.parseSeekTo(intent, value -> getMultiplePlayback(playback -> playback.seekTo(value)));
		MultiplePlaybackServiceIntentFactory.parseSwitchToSource(intent, value -> getMultiplePlayback(playback -> playback.playPlayerSource(value)));
		MultiplePlaybackServiceIntentFactory.parseRepeatSingle(intent, () -> getMultiplePlayback(MultiplePlayback::repeatSingle));
		MultiplePlaybackServiceIntentFactory.parseDoNotRepeatSingle(intent, () -> getMultiplePlayback(MultiplePlayback::doNotRepeatSingle));
		MultiplePlaybackServiceIntentFactory.parseShuffle(intent, () -> getMultiplePlayback(MultiplePlayback::shuffle));
		MultiplePlaybackServiceIntentFactory.parseDoNotShuffle(intent, () -> getMultiplePlayback(MultiplePlayback::doNotShuffle));
		MultiplePlaybackServiceIntentFactory.parseSimulateError(intent, () -> getMultiplePlayback(MultiplePlayback::simulateError));
		return START_STICKY;
	}

	private void initMultiplePlayback(MultiplePlaybackFactory factory, List<PlayerSource> playerSources) {
		this.multiplePlayback = Optional.of(factory.create(this));
		this.multiplePlayback.get().setMultiplePlaybackListener(new MultiplePlaybackListener() {
			@Override
			public void onUpdate(MultiplePlaybackState multiplePlaybackState) {
				multiplePlaybackListener.onUpdate(multiplePlaybackState);
				showForegroundNotification(multiplePlaybackState);
			}

			@Override
			public void onError(Throwable throwable) {
				multiplePlaybackListener.onError(throwable);
			}
		});
		this.multiplePlayback.get().setPlayerSources(playerSources);
	}

	public Optional<MultiplePlaybackState> getMultiplePlaybackState() {
		return this.multiplePlayback.transform(MultiplePlayback::getMultiplePlaybackState);
	}

	public void setMultiplePlaybackListener(MultiplePlaybackListener multiplePlaybackListener) {
		this.multiplePlaybackListener = multiplePlaybackListener != null ? multiplePlaybackListener : NullMultiplePlaybackListener.getInstance();
	}

	private void getMultiplePlayback(ParamAction<MultiplePlayback> success) {
		getMultiplePlayback(success, () -> {
		});
	}

	private void getMultiplePlayback(ParamAction<MultiplePlayback> success, Action fail) {
		if (this.multiplePlayback.isPresent()) {
			success.execute(this.multiplePlayback.get());
		} else {
			fail.execute();
		}
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}

	@Override
	public void onDestroy() {
		Log.d(getClass().getSimpleName(), "onDestroy");
		setMultiplePlaybackListener(null);
		getMultiplePlayback(MultiplePlayback::clear);
		this.multiplePlayback = Optional.absent();
		stopForeground(true);
		super.onDestroy();
	}

	private void showForegroundNotification(MultiplePlaybackState multiplePlaybackState) {
		PendingIntent playIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PLAY, MultiplePlaybackServiceIntentFactory.createPlay(this), 0);
		PendingIntent pauseIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PAUSE, MultiplePlaybackServiceIntentFactory.createPause(this), 0);
		PendingIntent stopIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_STOP, MultiplePlaybackServiceIntentFactory.createStop(this), 0);
		PendingIntent playNextIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PLAY_NEXT, MultiplePlaybackServiceIntentFactory.createStop(this), 0);
		PendingIntent playPreviousIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PLAY_PREVIOUS, MultiplePlaybackServiceIntentFactory.createStop(this), 0);

		startForeground(this.notificationId, this.multiplePlayerNotificationFactory.create(this, multiplePlaybackState, playIntent, pauseIntent, stopIntent, playNextIntent, playPreviousIntent));
	}

	public final class MultiplePlaybackServiceBinder extends Binder {
		public MultiplePlaybackService getService() {
			return MultiplePlaybackService.this;
		}
	}
}
