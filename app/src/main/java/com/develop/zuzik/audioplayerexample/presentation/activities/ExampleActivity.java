package com.develop.zuzik.audioplayerexample.presentation.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.playback.interfaces.PlaybackState;
import com.develop.zuzik.audioplayerexample.presentation.fragments.ExampleFragment;

public class ExampleActivity extends AppCompatActivity implements ExampleFragment.OnFragmentInteractionListener {

	Player.Model.Listener<Song> listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);

		findViewById(R.id.addLeft).setOnClickListener(v -> {
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.leftContainer);
			if (fragment == null) {
				fragment = new ExampleFragment();
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.leftContainer, fragment)
						.commit();
			}
		});
		findViewById(R.id.removeLeft).setOnClickListener(v -> {
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.leftContainer);
			if (fragment != null) {
				getSupportFragmentManager()
						.beginTransaction()
						.remove(fragment)
						.commit();
			}
		});

		findViewById(R.id.addRight).setOnClickListener(v -> {
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.rightContainer);
			if (fragment == null) {
				fragment = new ExampleFragment();
				getSupportFragmentManager()
						.beginTransaction()
						.add(R.id.rightContainer, fragment)
						.commit();
			}
		});
		findViewById(R.id.removeRight).setOnClickListener(v -> {
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.rightContainer);
			if (fragment != null) {
				getSupportFragmentManager()
						.beginTransaction()
						.remove(fragment)
						.commit();
			}
		});

		listener = new Player.Model.Listener<Song>() {
			@Override
			public void onUpdate(PlaybackState<Song> state) {
				Intent playIntent = new Intent("com.develop.zuzik.audioplayerexample.PLAY");
				Intent pauseIntent = new Intent("com.develop.zuzik.audioplayerexample.PAUSE");
				Intent stopIntent = new Intent("com.develop.zuzik.audioplayerexample.STOP");

				NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
						.setSmallIcon(R.mipmap.ic_launcher)
						.setContentTitle(state.playerSource.getSourceInfo().artist)
						.setContentText(state.playerSource.getSourceInfo().name)
						.setProgress(state.maxTimeInMilliseconds.or(100), state.currentTimeInMilliseconds, false)
						.addAction(0, "Play", PendingIntent.getBroadcast(getApplicationContext(), 100, playIntent, 0))
						.addAction(0, "Pause", PendingIntent.getBroadcast(getApplicationContext(), 100, pauseIntent, 0))
						.addAction(0, "Stop", PendingIntent.getBroadcast(getApplicationContext(), 100, stopIntent, 0));
				((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
						.notify(100, builder.build());
			}

			@Override
			public void onError(Throwable error) {

			}
		};
		getModel().addListener(listener);

		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				getModel().play();
			}
		}, new IntentFilter("com.develop.zuzik.audioplayerexample.PLAY"));
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				getModel().pause();
			}
		}, new IntentFilter("com.develop.zuzik.audioplayerexample.PAUSE"));
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				getModel().stop();
			}
		}, new IntentFilter("com.develop.zuzik.audioplayerexample.STOP"));
	}

	@Override
	protected void onDestroy() {
		getModel().removeListener(this.listener);
		super.onDestroy();
	}

	@Override
	public Player.Model<Song> getModel() {
		return ((App) getApplicationContext()).getModel();
	}
}