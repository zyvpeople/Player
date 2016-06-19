package com.develop.zuzik.audioplayerexample.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.mvp.implementations.models.PlayerModel;
import com.develop.zuzik.audioplayerexample.mvp.implementations.presenters.PlayerPresenter;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.exceptions.AudioServiceNotSupportException;
import com.develop.zuzik.audioplayerexample.player.player_source.RawResourcePlayerSource;
import com.develop.zuzik.audioplayerexample.presentation.player_exception_message_provider.ExamplePlayerExceptionMessageProvider;

public class ExampleActivity extends AppCompatActivity implements Player.View<Song> {

	private TextView title;
	private Button play;
	private Button pause;
	private Button stop;
	private Button fakeError;
	private SeekBar seekBar;
	private TextView maxDuration;
	private TextView currentPosition;
	private SwitchCompat repeat;
	private View loading;

	private Player.Presenter<Song> presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);

		try {
			this.presenter = new PlayerPresenter<Song>(
					new PlayerModel<>(this, new RawResourcePlayerSource<>(new Song("Of monsters and men", "Crystal (long)", R.drawable.of_monsters_and_men_1), R.raw.song)),
					new ExamplePlayerExceptionMessageProvider());
		} catch (AudioServiceNotSupportException e) {
			throw new RuntimeException(e);
		}

		this.title = (TextView) findViewById(R.id.title);
		this.play = (Button) findViewById(R.id.play);
		this.pause = (Button) findViewById(R.id.pause);
		this.stop = (Button) findViewById(R.id.stop);
		this.fakeError = (Button) findViewById(R.id.fakeError);
		this.seekBar = (SeekBar) findViewById(R.id.seekBar);
		this.maxDuration = (TextView) findViewById(R.id.maxDuration);
		this.currentPosition = (TextView) findViewById(R.id.currentPosition);
		this.repeat = (SwitchCompat) findViewById(R.id.repeat);
		this.loading = findViewById(R.id.loading);

		this.play.setOnClickListener(v -> this.presenter.onPlay());
		this.pause.setOnClickListener(v -> this.presenter.onPause());
		this.stop.setOnClickListener(v -> this.presenter.onStop());
		this.fakeError.setOnClickListener(v -> this.presenter.simulateError());
		this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					presenter.onSeekToPosition(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});
		this.repeat.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				this.presenter.onRepeat();
			} else {
				this.presenter.onDoNotRepeat();
			}
		});

		this.presenter.onInit(this);
	}

	@Override
	protected void onDestroy() {
		this.presenter.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.presenter.onAppear();
	}

	@Override
	protected void onStop() {
		this.presenter.onDisappear();
		super.onStop();
	}

	//region Player.View

	@Override
	public void setRepeat() {
		this.repeat.setChecked(true);
	}

	@Override
	public void setDoNotRepeat() {
		this.repeat.setChecked(false);
	}

	@Override
	public void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds) {
		this.seekBar.setProgress(currentTimeInMilliseconds);
		this.seekBar.setMax(totalTimeInMilliseconds);
	}

	@Override
	public void showProgress() {
		this.seekBar.setEnabled(true);
	}

	@Override
	public void hideProgress() {
		this.seekBar.setEnabled(false);
	}

	@Override
	public void showTime(String currentTime, String totalTime) {
		this.currentPosition.setText(currentTime);
		this.maxDuration.setText(totalTime);
	}

	@Override
	public void showError(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void enablePlayControls(boolean play, boolean pause, boolean stop) {
		this.play.setEnabled(play);
		this.pause.setEnabled(pause);
		this.stop.setEnabled(stop);
	}

	@Override
	public void showLoading() {
		this.loading.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideLoading() {
		this.loading.setVisibility(View.GONE);
	}

	@Override
	public void displayCurrentSource(Song song) {
		this.title.setText(song.name);
	}

	//endregion
}