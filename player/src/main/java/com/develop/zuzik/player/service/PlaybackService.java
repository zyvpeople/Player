package com.develop.zuzik.player.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.Playback;
import com.develop.zuzik.player.interfaces.PlaybackFactory;
import com.develop.zuzik.player.interfaces.PlaybackListener;
import com.develop.zuzik.player.interfaces.PlaybackState;
import com.develop.zuzik.player.interfaces.PlayerNotificationFactory;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.null_object.NullPlaybackListener;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.functions.Function;
import com.fernandocejas.arrow.optional.Optional;

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
	private PlaybackListener playbackListener = NullPlaybackListener.getInstance();
	private int notificationId;
	private PlayerNotificationFactory playerNotificationFactory;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(getClass().getSimpleName(), "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(getClass().getSimpleName(), "onStartCommand");
		PlaybackServiceIntentFactory.parseForInit(intent, new ParamAction<PlaybackServiceInitializeBundle>() {
			@Override
			public void execute(PlaybackServiceInitializeBundle bundle) {
				PlayerSource source = bundle.playerSource;
				PlaybackFactory factory = bundle.playbackFactory;
				boolean repeat = bundle.repeat;
				notificationId = bundle.notificationId;
				playerNotificationFactory = bundle.playerNotificationFactory;
				if (playback.isPresent()) {
					if (!playback.get().getPlaybackState().playerSource.equals(source)) {
						playback.get().release();
						initPlayback(source, factory, repeat);
					}
				} else {
					initPlayback(source, factory, repeat);
				}

			}
		});
		PlaybackServiceIntentFactory.parsePlay(intent, new Action() {
			@Override
			public void execute() {
				getPlayback(new ParamAction<Playback>() {
					@Override
					public void execute(Playback value) {
						value.play();
					}
				});
			}
		});
		PlaybackServiceIntentFactory.parsePause(intent, new Action() {
			@Override
			public void execute() {
				getPlayback(new ParamAction<Playback>() {
					@Override
					public void execute(Playback value) {
						value.pause();
					}
				});
			}
		});
		PlaybackServiceIntentFactory.parseStop(intent, new Action() {
			@Override
			public void execute() {
				getPlayback(new ParamAction<Playback>() {
					@Override
					public void execute(Playback value) {
						value.stop();
					}
				});
			}
		});
		PlaybackServiceIntentFactory.parseSeekTo(intent, new ParamAction<Integer>() {
			@Override
			public void execute(final Integer value) {
				getPlayback(new ParamAction<Playback>() {
					@Override
					public void execute(Playback playback) {
						playback.seekTo(value);
					}
				});
			}
		});
		PlaybackServiceIntentFactory.parseRepeat(intent, new Action() {
			@Override
			public void execute() {
				getPlayback(new ParamAction<Playback>() {
					@Override
					public void execute(Playback value) {
						value.repeat();
					}
				});
			}
		});
		PlaybackServiceIntentFactory.parseDoNotRepeat(intent, new Action() {
			@Override
			public void execute() {
				getPlayback(new ParamAction<Playback>() {
					@Override
					public void execute(Playback value) {
						value.doNotRepeat();
					}
				});
			}
		});
		PlaybackServiceIntentFactory.parseSimulateError(intent, new Action() {
			@Override
			public void execute() {
				getPlayback(new ParamAction<Playback>() {
					@Override
					public void execute(Playback value) {
						value.simulateError();
					}
				});
			}
		});
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
		return this.playback.transform(new Function<Playback, PlaybackState>() {
			@org.jetbrains.annotations.Nullable
			@Override
			public PlaybackState apply(Playback input) {
				return input.getPlaybackState();
			}
		});
	}

	public void videoViewSetter(ParamAction<VideoViewSetter> success) {
		if (this.playback.isPresent()) {
			this.playback.get().videoViewSetter(success);
		}
	}

	public void setPlaybackListener(PlaybackListener playbackListener) {
		this.playbackListener = playbackListener != null ? playbackListener : NullPlaybackListener.getInstance();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return this.binder;
	}

	@Override
	public void onDestroy() {
		Log.d(getClass().getSimpleName(), "onDestroy");
		setPlaybackListener(NullPlaybackListener.getInstance());
		getPlayback(new ParamAction<Playback>() {
			@Override
			public void execute(Playback value) {
				value.release();
			}
		});
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
		PendingIntent playIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PLAY, PlaybackServiceIntentFactory.createPlay(this), 0);
		PendingIntent pauseIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PAUSE, PlaybackServiceIntentFactory.createPause(this), 0);
		PendingIntent stopIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_STOP, PlaybackServiceIntentFactory.createStop(this), 0);

		startForeground(this.notificationId, this.playerNotificationFactory.create(this, playbackState, playIntent, pauseIntent, stopIntent));
	}

	public final class PlaybackServiceBinder extends Binder {
		public PlaybackService getService() {
			return PlaybackService.this;
		}
	}
}
