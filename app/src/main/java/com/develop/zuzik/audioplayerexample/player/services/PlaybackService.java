package com.develop.zuzik.audioplayerexample.player.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.interfaces.ParamAction;
import com.develop.zuzik.audioplayerexample.player.notification.NotificationFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.null_objects.NullPlaybackListener;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.fernandocejas.arrow.optional.Optional;

import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createPause;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createPlay;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.createStop;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.parseDoNotRepeat;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.parseForInit;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.parsePause;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.parsePlay;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.parseRepeat;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.parseSeekTo;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.parseSimulateError;
import static com.develop.zuzik.audioplayerexample.player.services.PlaybackServiceIntentFactory.parseStop;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class PlaybackService extends Service {

	private static final int PENDING_INTENT_ID_PLAY = 1;
	private static final int PENDING_INTENT_ID_PAUSE = 2;
	private static final int PENDING_INTENT_ID_STOP = 3;

	private final IBinder binder = new PlaybackServiceBinder();
	private Optional<Playback> playback = Optional.absent();
	private PlaybackListener playbackListener = new NullPlaybackListener<>();
	private int notificationId;
	private NotificationFactory notificationFactory;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(getClass().getSimpleName(), "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(getClass().getSimpleName(), "onStartCommand");
		parseForInit(intent, bundle -> {
			PlayerSource source = bundle.playerSource;
			PlaybackFactory factory = bundle.playbackFactory;
			boolean repeat = bundle.repeat;
			this.notificationId = bundle.notificationId;
			this.notificationFactory = bundle.notificationFactory;
			if (this.playback.isPresent()) {
				if (!this.playback.get().getPlaybackState().playerSource.equals(source)) {
					this.playback.get().release();
					initPlayback(source, factory, repeat);
				}
			} else {
				initPlayback(source, factory, repeat);
			}
		});
		parsePlay(intent, () -> getPlayback(Playback::play));
		parsePause(intent, () -> getPlayback(Playback::pause));
		parseStop(intent, () -> getPlayback(Playback::stop));
		parseSeekTo(intent, value -> getPlayback(playback -> playback.seekTo(value)));
		parseRepeat(intent, () -> getPlayback(Playback::repeat));
		parseDoNotRepeat(intent, () -> getPlayback(Playback::doNotRepeat));
		parseSimulateError(intent, () -> getPlayback(Playback::simulateError));
		return START_STICKY;
	}

	private void initPlayback(PlayerSource source, PlaybackFactory factory, boolean repeat) {
		this.playback = Optional.of(factory.create(this, repeat, source));
		this.playback.get().setPlaybackListener(new PlaybackListener() {
			@Override
			public void onUpdate(PlaybackState playbackState) {
				playbackListener.onUpdate(playbackState);
				showForegroundNotification(playbackState);
			}

			@Override
			public void onError(Throwable throwable) {
				playbackListener.onError(throwable);
			}
		});
		this.playback.get().init();
	}

	public Optional<PlaybackState> getPlaybackState() {
		return this.playback.transform(Playback::getPlaybackState);
	}

	public void setPlaybackListener(PlaybackListener playbackListener) {
		this.playbackListener = playbackListener != null ? playbackListener : new NullPlaybackListener<>();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}

	@Override
	public void onDestroy() {
		Log.d(getClass().getSimpleName(), "onDestroy");
		setPlaybackListener(null);
		getPlayback(Playback::release);
		this.playback = Optional.absent();
		stopForeground(true);
		super.onDestroy();
	}

	private void getPlayback(ParamAction<Playback> success) {
		if (this.playback.isPresent()) {
			success.execute(this.playback.get());
		}
	}

	private void showForegroundNotification(PlaybackState playbackState) {
		PendingIntent playIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PLAY, createPlay(this), 0);
		PendingIntent pauseIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PAUSE, createPause(this), 0);
		PendingIntent stopIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_STOP, createStop(this), 0);

		startForeground(this.notificationId, this.notificationFactory.create(this, playbackState, playIntent, pauseIntent, stopIntent));
	}

	public final class PlaybackServiceBinder extends Binder {
		public PlaybackService getService() {
			return PlaybackService.this;
		}
	}
}
