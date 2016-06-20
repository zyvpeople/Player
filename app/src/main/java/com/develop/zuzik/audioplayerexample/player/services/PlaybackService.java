package com.develop.zuzik.audioplayerexample.player.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.exceptions.AudioServiceNotSupportException;
import com.develop.zuzik.audioplayerexample.player.playback.Playback;
import com.develop.zuzik.audioplayerexample.player.playback.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class PlaybackService extends Service {

	private Optional<Playback> playback = Optional.absent();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(getClass().getSimpleName(), "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(getClass().getSimpleName(), "onStartCommand");
		PlaybackServiceIntentFactory.parsePlayerSource(intent, value -> {
			if (this.playback.isPresent()) {
				if (!this.playback.get().getPlayerSource().equals(value)) {
					this.playback.get().release();
					initPlayback(value);
				}
			} else {
				initPlayback(value);
			}
		});
		PlaybackServiceIntentFactory.parsePlay(intent, () -> getPlayback(Playback::play));
		PlaybackServiceIntentFactory.parsePause(intent, () -> getPlayback(Playback::pause));
		PlaybackServiceIntentFactory.parseStop(intent, () -> getPlayback(Playback::stop));
		PlaybackServiceIntentFactory.parseSeekTo(intent, value -> getPlayback(playback -> playback.seekTo(value)));
		PlaybackServiceIntentFactory.parseRepeat(intent, () -> getPlayback(Playback::repeat));
		PlaybackServiceIntentFactory.parseDoNotRepeat(intent, () -> getPlayback(Playback::doNotRepeat));
		return START_STICKY;
	}

	private void initPlayback(PlayerSource value) {
		try {
			this.playback = Optional.of(new Playback(this, value));
			this.playback.get().setPlaybackListener(new PlaybackListener() {
				@Override
				public void onUpdate() {
					LocalBroadcastManager
							.getInstance(getApplicationContext())
							.sendBroadcast(PlaybackServiceBroadcastIntentFactory.createPlaybackState(playback.get().getPlaybackState()));
				}

				@Override
				public void onError(Throwable throwable) {
					LocalBroadcastManager
							.getInstance(getApplicationContext())
							.sendBroadcast(PlaybackServiceBroadcastIntentFactory.createError(throwable));
				}
			});
			this.playback.get().init();
		} catch (AudioServiceNotSupportException e) {
			this.playback = Optional.absent();
		}
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		getPlayback(Playback::release);
		super.onDestroy();
	}

	private void getPlayback(ParamAction<Playback> success) {
		if (this.playback.isPresent()) {
			success.execute(this.playback.get());
		}
	}
}
