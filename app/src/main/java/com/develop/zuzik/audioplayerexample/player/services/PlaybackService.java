package com.develop.zuzik.audioplayerexample.player.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.null_objects.NullPlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.settings.InMemoryPlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class PlaybackService extends Service {

	private final IBinder binder = new PlaybackServiceBinder();
	private Optional<Playback> playback = Optional.absent();
	private PlaybackListener playbackListener = new NullPlaybackListener<>();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(getClass().getSimpleName(), "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(getClass().getSimpleName(), "onStartCommand");
		PlaybackServiceIntentFactory.parseForSetSource(intent, (playerSource, playbackFactory) -> {
			if (this.playback.isPresent()) {
				if (!this.playback.get().getPlaybackState().playerSource.equals(playerSource)) {
					this.playback.get().release();
					initPlayback(playerSource, playbackFactory);
				}
			} else {
				initPlayback(playerSource, playbackFactory);
			}
		});
		PlaybackServiceIntentFactory.parsePlay(intent, () -> getPlayback(Playback::play));
		PlaybackServiceIntentFactory.parsePause(intent, () -> getPlayback(Playback::pause));
		PlaybackServiceIntentFactory.parseStop(intent, () -> getPlayback(Playback::stop));
		PlaybackServiceIntentFactory.parseSeekTo(intent, value -> getPlayback(playback -> playback.seekTo(value)));
		PlaybackServiceIntentFactory.parseRepeat(intent, () -> getPlayback(Playback::repeat));
		PlaybackServiceIntentFactory.parseDoNotRepeat(intent, () -> getPlayback(Playback::doNotRepeat));
		PlaybackServiceIntentFactory.parseSimulateError(intent, () -> getPlayback(Playback::simulateError));
		return START_STICKY;
	}

	private void initPlayback(PlayerSource source, PlaybackFactory factory) {
		//TODO: do not create here but pass as argument
		InMemoryPlaybackSettings settings = new InMemoryPlaybackSettings();
		this.playback = Optional.of(factory.create(this, settings, source));
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
		setPlaybackListener(null);
		getPlayback(Playback::release);
		this.playback = Optional.absent();
		super.onDestroy();
	}

	private void getPlayback(ParamAction<Playback> success) {
		if (this.playback.isPresent()) {
			success.execute(this.playback.get());
		}
	}

	//TODO: create factory for notification
	private void showForegroundNotification(PlaybackState playbackState) {
		int id = 100500;
		Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

		Notification notification = new NotificationCompat.Builder(PlaybackService.this)
				.setContentTitle(((Song) playbackState.playerSource.getSourceInfo()).artist)
				.setContentText(((Song) playbackState.playerSource.getSourceInfo()).name)
				.setTicker("Ticker")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
				.setProgress((Integer) playbackState.maxTimeInMilliseconds.or(100), playbackState.currentTimeInMilliseconds, false)
				.setOngoing(true)
				.addAction(0, "Play", PendingIntent.getService(getApplicationContext(), id, PlaybackServiceIntentFactory.createPlay(this), 0))
				.addAction(0, "Pause", PendingIntent.getService(getApplicationContext(), id, PlaybackServiceIntentFactory.createPause(this), 0))
				.addAction(0, "Stop", PendingIntent.getService(getApplicationContext(), id, PlaybackServiceIntentFactory.createStop(this), 0))
				.build();
		startForeground(id, notification);
	}

	public final class PlaybackServiceBinder extends Binder {
		public PlaybackService getService() {
			return PlaybackService.this;
		}
	}
}
