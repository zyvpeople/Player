package com.develop.zuzik.audioplayerexample.player.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackListener;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.player.playback.local.LocalPlayback;
import com.develop.zuzik.audioplayerexample.player.playback.settings.InMemoryPlaybackSettings;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/20/16
 */
//FIXME:is not implemented
public class PlaybackService extends Service {

	private Optional<LocalPlayback> playback = Optional.absent();

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
		PlaybackServiceIntentFactory.parsePlay(intent, () -> getPlayback(LocalPlayback::play));
		PlaybackServiceIntentFactory.parsePause(intent, () -> getPlayback(LocalPlayback::pause));
		PlaybackServiceIntentFactory.parseStop(intent, () -> getPlayback(LocalPlayback::stop));
		PlaybackServiceIntentFactory.parseSeekTo(intent, value -> getPlayback(playback -> playback.seekTo(value)));
		PlaybackServiceIntentFactory.parseRepeat(intent, () -> getPlayback(LocalPlayback::repeat));
		PlaybackServiceIntentFactory.parseDoNotRepeat(intent, () -> getPlayback(LocalPlayback::doNotRepeat));
		return START_STICKY;
	}

	private void initPlayback(PlayerSource value) {
		this.playback = Optional.of(new LocalPlayback(this, new InMemoryPlaybackSettings(), value));
		this.playback.get().setPlaybackListener(new PlaybackListener() {

			@Override
			public void onUpdate(PlaybackState playbackState) {
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
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		getPlayback(LocalPlayback::release);
		super.onDestroy();
	}

	private void getPlayback(ParamAction<LocalPlayback> success) {
		if (this.playback.isPresent()) {
			success.execute(this.playback.get());
		}
	}
}
