package com.develop.zuzik.audioplayerexample.player.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.mvp.implementations.models.PlayerModel;
import com.develop.zuzik.audioplayerexample.mvp.implementations.presenters.PlayerPresenter;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.PlayerExceptionMessageProvider;
import com.develop.zuzik.audioplayerexample.player.exceptions.AudioServiceNotSupportException;
import com.develop.zuzik.audioplayerexample.player.player_source.PlayerSource;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.Action;
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class PlaybackService extends Service {

	private static final String EXTRA_ACTION = "EXTRA_ACTION";
	private static final String EXTRA_PLAYER_SOURCE = "EXTRA_PLAYER_SOURCE";
	private static final String ACTION_INIT = "ACTION_INIT";
	private static final String ACTION_PLAY = "ACTION_PLAY";
	private static final String ACTION_PAUSE = "ACTION_PAUSE";
	private static final String ACTION_STOP = "ACTION_STOP";

	public static Intent createForInit(Context context, PlayerSource playerSource) {
		Intent intent = new Intent(context, PlaybackService.class);
		intent.putExtra(EXTRA_ACTION, ACTION_INIT);
		intent.putExtra(EXTRA_PLAYER_SOURCE, playerSource);
		return intent;
	}

	private static void parseForInit(Intent intent, ParamAction<PlayerSource> success) {
		parseAction(intent, ACTION_INIT, () ->
				parsePlayerSource(intent, success));
	}

	public static Intent createForDestroy(Context context) {
		return new Intent(context, PlaybackService.class);
	}

	public static Intent createForPlay(Context context) {
		Intent intent = new Intent(context, PlaybackService.class);
		intent.putExtra(EXTRA_ACTION, ACTION_PLAY);
		return intent;
	}

	private static void parsePlay(Intent intent, Action success) {
		parseAction(intent, ACTION_PLAY, success);
	}

	public static Intent createForPause(Context context) {
		Intent intent = new Intent(context, PlaybackService.class);
		intent.putExtra(EXTRA_ACTION, ACTION_PAUSE);
		return intent;
	}

	private static void parsePause(Intent intent, Action success) {
		parseAction(intent, ACTION_PAUSE, success);
	}

	public static Intent createForStop(Context context) {
		Intent intent = new Intent(context, PlaybackService.class);
		intent.putExtra(EXTRA_ACTION, ACTION_STOP);
		return intent;
	}

	private static void parseStop(Intent intent, Action success) {
		parseAction(intent, ACTION_STOP, success);
	}

	private static void parseAction(Intent intent, String action, Action success) {
		if (intent != null && action.equals(intent.getStringExtra(EXTRA_ACTION))) {
			success.execute();
		}
	}

	private static void parsePlayerSource(Intent intent, ParamAction<PlayerSource> success) {
		if (intent != null && intent.hasExtra(EXTRA_PLAYER_SOURCE)) {
			success.execute((PlayerSource) intent.getSerializableExtra(EXTRA_PLAYER_SOURCE));
		}
	}

	private Optional<Player.Presenter> presenter = Optional.absent();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(getClass().getSimpleName(), "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(getClass().getSimpleName(), "onStartCommand");
		parseForInit(intent, value -> {
			getPresenter(presenter -> {
				presenter.onDisappear();
				presenter.onDestroy();
			});
			try {
				this.presenter = Optional.of(
						new PlayerPresenter(
								new PlayerModel<>(getApplicationContext(), value), this.emptyExceptionMessageProvider));
				this.presenter.get().onInit(this.foregroundNotificationView);
				this.presenter.get().onAppear();
			} catch (AudioServiceNotSupportException e) {
			}
		});
		parsePlay(intent, () -> getPresenter(Player.Presenter::onPlay));
		parsePause(intent, () -> getPresenter(Player.Presenter::onPause));
		parseStop(intent, () -> getPresenter(Player.Presenter::onStop));
		return START_STICKY;
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Log.i(getClass().getSimpleName(), "onDestroy");
		getPresenter(Player.Presenter::onDisappear);
		getPresenter(Player.Presenter::onDestroy);
		super.onDestroy();
	}

	private void getPresenter(ParamAction<Player.Presenter> success) {
		if (this.presenter.isPresent()) {
			success.execute(this.presenter.get());
		}
	}

	private final Player.View foregroundNotificationView = new Player.View() {
		@Override
		public void setRepeat() {

		}

		@Override
		public void setDoNotRepeat() {

		}

		@Override
		public void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds) {

		}

		@Override
		public void showProgress() {

		}

		@Override
		public void hideProgress() {

		}

		@Override
		public void showTime(String currentTime, String totalTime) {

		}

		@Override
		public void showError(String message) {

		}

		@Override
		public void enablePlayControls(boolean play, boolean pause, boolean stop) {

		}

		@Override
		public void showLoading() {

		}

		@Override
		public void hideLoading() {

		}

		@Override
		public void displayCurrentSource(Object o) {

		}

		@Override
		public void display(ViewData viewData) {
			int id = 100500;
			Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

			Notification notification = new NotificationCompat.Builder(PlaybackService.this)
					.setContentTitle(((Song) viewData.sourceInfo).artist)
					.setContentText(((Song) viewData.sourceInfo).name)
					.setTicker("Ticker")
					.setSmallIcon(R.mipmap.ic_launcher)
					.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
					.setProgress(viewData.totalTimeInMilliseconds, viewData.currentTimeInMilliseconds, false)
//				.setContentIntent(pendingIntent)
//				.setOngoing(true)
//				.addAction(android.R.drawable.ic_media_previous,
//						"Previous", ppreviousIntent)
//				.addAction(android.R.drawable.ic_media_play, "Play",
//						pplayIntent)
//				.addAction(android.R.drawable.ic_media_next, "Next",
//						pnextIntent)
					.build();
			startForeground(id, notification);
		}
	};

	private final PlayerExceptionMessageProvider emptyExceptionMessageProvider = new PlayerExceptionMessageProvider() {
		@Override
		public String playerInitializeExceptionMessage() {
			return "";
		}

		@Override
		public String failRequestAudioFocusExceptionMessage() {
			return "";
		}

		@Override
		public String audioFocusLostExceptionMessage() {
			return "";
		}

		@Override
		public String mediaPlayerStateExceptionMessage() {
			return "";
		}

		@Override
		public String fakeExceptionMessage() {
			return "";
		}

		@Override
		public String unknownExceptionMessage() {
			return "";
		}
	};
}
