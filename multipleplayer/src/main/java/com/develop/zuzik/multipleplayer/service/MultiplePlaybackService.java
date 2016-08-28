package com.develop.zuzik.multipleplayer.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayback;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackFactory;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackListener;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackServiceListener;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlaybackState;
import com.develop.zuzik.multipleplayer.interfaces.MultiplePlayerNotificationFactory;
import com.develop.zuzik.multipleplayer.interfaces.PlayerSourceReleaseStrategy;
import com.develop.zuzik.multipleplayer.null_object.NullMultiplePlaybackListener;
import com.develop.zuzik.multipleplayer.null_object.NullMultiplePlaybackServiceListener;
import com.develop.zuzik.player.interfaces.Action;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.null_object.NullAction;
import com.develop.zuzik.player.source.PlayerSource;
import com.fernandocejas.arrow.functions.Function;
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
	private static final int PENDING_INTENT_ID_DESTROY_SERVICE = 6;

	private final IBinder binder = new MultiplePlaybackServiceBinder();
	private Optional<MultiplePlayback> multiplePlayback = Optional.absent();
	private MultiplePlaybackListener multiplePlaybackListener = NullMultiplePlaybackListener.getInstance();
	private MultiplePlaybackServiceListener multiplePlaybackServiceListener = NullMultiplePlaybackServiceListener.INSTANCE;
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
		MultiplePlaybackServiceIntentFactory.parseForInit(intent, new ParamAction<MultiplePlaybackServiceInitializeBundle>() {
			@Override
			public void execute(MultiplePlaybackServiceInitializeBundle bundle) {
				MultiplePlaybackFactory multiplePlaybackFactory = bundle.multiplePlaybackFactory;
				List<PlayerSource> playerSources = bundle.playerSources;
				MultiplePlaybackService.this.notificationId = bundle.notificationId;
				MultiplePlaybackService.this.multiplePlayerNotificationFactory = bundle.multiplePlayerNotificationFactory;
				if (MultiplePlaybackService.this.multiplePlayback.isPresent()) {
					MultiplePlaybackService.this.multiplePlayback.get().setPlayerSources(playerSources, bundle.releaseStrategy);
				} else {
					MultiplePlaybackService.this.initMultiplePlayback(multiplePlaybackFactory, playerSources, bundle.releaseStrategy);
				}
			}
		});
		MultiplePlaybackServiceIntentFactory.parseForDestroy(intent, new Action() {
			@Override
			public void execute() {
				multiplePlaybackServiceListener.onReceiveDestroyCommand();
			}
		});
		MultiplePlaybackServiceIntentFactory.parsePlay(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.play();
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parsePause(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.pause();
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parseStop(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.stop();
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parsePlayNext(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.playNextPlayerSource();
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parsePlayPrevious(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.playPreviousPlayerSource();
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parseSeekTo(intent, new ParamAction<Integer>() {
			@Override
			public void execute(final Integer value) {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback playback) {
						playback.seekTo(value);
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parseSwitchToSource(intent, new ParamAction<PlayerSource>() {
			@Override
			public void execute(final PlayerSource value) {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback playback) {
						playback.playPlayerSource(value);
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parseRepeatSingle(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.repeatSingle();
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parseDoNotRepeatSingle(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.doNotRepeatSingle();
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parseShuffle(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.shuffle();
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parseDoNotShuffle(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.doNotShuffle();
					}
				});
			}
		});
		MultiplePlaybackServiceIntentFactory.parseSimulateError(intent, new Action() {
			@Override
			public void execute() {
				MultiplePlaybackService.this.getMultiplePlayback(new ParamAction<MultiplePlayback>() {
					@Override
					public void execute(MultiplePlayback multiplePlayback1) {
						multiplePlayback1.simulateError();
					}
				});
			}
		});
		return START_STICKY;
	}

	private void initMultiplePlayback(MultiplePlaybackFactory factory, List<PlayerSource> playerSources, PlayerSourceReleaseStrategy releaseStrategy) {
		this.multiplePlayback = Optional.of(factory.create(this));
		this.multiplePlayback.get().setMultiplePlaybackListener(new MultiplePlaybackListener() {
			@Override
			public void onUpdate(MultiplePlaybackState multiplePlaybackState) {
				multiplePlaybackListener.onUpdate(multiplePlaybackState);
				if (multiplePlaybackState.currentPlaybackState.isPresent()) {
					showForegroundNotification(multiplePlaybackState);
				} else {
					stopForeground(true);
				}
			}

			@Override
			public void onError(Throwable throwable) {
				multiplePlaybackListener.onError(throwable);
			}
		});
		this.multiplePlayback.get().setPlayerSources(playerSources, releaseStrategy);
	}

	public Optional<MultiplePlaybackState> getMultiplePlaybackState() {
		return this.multiplePlayback.transform(new Function<MultiplePlayback, MultiplePlaybackState>() {
			@Override
			public MultiplePlaybackState apply(MultiplePlayback input) {
				return input.getMultiplePlaybackState();
			}
		});
	}

	public void videoViewSetter(ParamAction<VideoViewSetter> success) {
		if (this.multiplePlayback.isPresent()) {
			this.multiplePlayback.get().videoViewSetter(success);
		}
	}

	public void setMultiplePlaybackListener(MultiplePlaybackListener multiplePlaybackListener) {
		this.multiplePlaybackListener = multiplePlaybackListener != null ? multiplePlaybackListener : NullMultiplePlaybackListener.getInstance();
	}

	public void setMultiplePlaybackServiceListener(MultiplePlaybackServiceListener listener) {
		this.multiplePlaybackServiceListener = listener != null ? listener : NullMultiplePlaybackServiceListener.INSTANCE;
	}

	private void getMultiplePlayback(ParamAction<MultiplePlayback> success) {
		getMultiplePlayback(success, NullAction.INSTANCE);
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
		getMultiplePlayback(new ParamAction<MultiplePlayback>() {
			@Override
			public void execute(MultiplePlayback multiplePlayback1) {
				multiplePlayback1.release();
			}
		});
		this.multiplePlayback = Optional.absent();
		stopForeground(true);
		setMultiplePlaybackListener(null);
		setMultiplePlaybackServiceListener(null);
		super.onDestroy();
	}

	private void showForegroundNotification(MultiplePlaybackState multiplePlaybackState) {
		PendingIntent playIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PLAY, MultiplePlaybackServiceIntentFactory.createPlay(this), 0);
		PendingIntent pauseIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PAUSE, MultiplePlaybackServiceIntentFactory.createPause(this), 0);
		PendingIntent stopIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_STOP, MultiplePlaybackServiceIntentFactory.createStop(this), 0);
		PendingIntent playNextIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PLAY_NEXT, MultiplePlaybackServiceIntentFactory.createPlayNext(this), 0);
		PendingIntent playPreviousIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_PLAY_PREVIOUS, MultiplePlaybackServiceIntentFactory.createPlayPrevious(this), 0);
		PendingIntent destroyServiceIntent = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_ID_DESTROY_SERVICE, MultiplePlaybackServiceIntentFactory.createForDestroy(this), 0);

		startForeground(
				this.notificationId,
				this.multiplePlayerNotificationFactory
						.create(
								this,
								multiplePlaybackState,
								playIntent,
								pauseIntent,
								stopIntent,
								playNextIntent,
								playPreviousIntent,
								destroyServiceIntent));
	}

	public final class MultiplePlaybackServiceBinder extends Binder {
		public MultiplePlaybackService getService() {
			return MultiplePlaybackService.this;
		}
	}
}
