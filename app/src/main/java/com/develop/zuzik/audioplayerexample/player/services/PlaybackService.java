package com.develop.zuzik.audioplayerexample.player.services;

import android.app.Notification;
import android.app.Service;
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
import com.develop.zuzik.audioplayerexample.player.player_states.interfaces.ParamAction;
import com.fernandocejas.arrow.optional.Optional;

/**
 * User: zuzik
 * Date: 6/20/16
 */
public class PlaybackService extends Service {

	private Optional<Player.Presenter> presenter = Optional.absent();

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(getClass().getSimpleName(), "onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(getClass().getSimpleName(), "onStartCommand");
		PlaybackServiceIntentFactory.parsePlayerSource(intent, value -> {
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
		PlaybackServiceIntentFactory.parsePlay(intent, () -> getPresenter(Player.Presenter::onPlay));
		PlaybackServiceIntentFactory.parsePause(intent, () -> getPresenter(Player.Presenter::onPause));
		PlaybackServiceIntentFactory.parseStop(intent, () -> getPresenter(Player.Presenter::onStop));
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
