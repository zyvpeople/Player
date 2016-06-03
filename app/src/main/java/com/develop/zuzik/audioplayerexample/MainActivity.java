package com.develop.zuzik.audioplayerexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.develop.zuzik.audioplayerexample.player.Playback;

public class MainActivity extends AppCompatActivity {

	private Button play;
	private Button pause;
	private Button stop;
	private Button fakeError;
	private SeekBar seekBar;
	private TextView maxDuration;
	private TextView currentPosition;

	Playback playback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.play = (Button) findViewById(R.id.play);
		this.pause = (Button) findViewById(R.id.pause);
		this.stop = (Button) findViewById(R.id.stop);
		this.fakeError = (Button) findViewById(R.id.fakeError);
		this.seekBar = (SeekBar) findViewById(R.id.seekBar);
		this.maxDuration = (TextView) findViewById(R.id.maxDuration);
		this.currentPosition = (TextView) findViewById(R.id.currentPosition);

		this.play.setOnClickListener(v -> playback.play(MainActivity.this));
		this.pause.setOnClickListener(v -> playback.pause());
		this.stop.setOnClickListener(v -> playback.stop());
		this.fakeError.setOnClickListener(v -> playback.fakeError());
		this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					playback.seekTo(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

//		this.playback = new Playback(R.raw.song_short);
		this.playback = new Playback(R.raw.song);
//		this.playback = new Playback(Uri.parse("http://storage.mp3.cc/download/454079/dG5Dd2NNMy8vZ1NUc2hINFZtRXl4OUt4c2RjZXhvdmkra3liTmFnOTFWMlZibUlCMlZRTXcwcVVhckszaldDSGRqMzRLaTg2ckpkQVhxZHYya3NKc09MM0VvNnFFQ2g3ZnNUYTlMS3M2YlY5MkhtcEpYTlR4V1JPaUJUcHhWMU8/Of_Monsters_And_Men-Little_Talks_(mp3.cc).mp3"));

		this.playback.init();
	}

	@Override
	protected void onStart() {
		super.onStart();

		this.playback.setPlaybackListener(bundle -> {
			//TODO:configure buttons for state

			maxDuration.setText(bundle.maxDurationInMilliseconds != null
					? String.valueOf(bundle.maxDurationInMilliseconds)
					: "-");
			currentPosition.setText(String.valueOf(bundle.currentPositionInMilliseconds));
			seekBar.setEnabled(bundle.maxDurationInMilliseconds != null);
			seekBar.setMax(bundle.maxDurationInMilliseconds != null
					? bundle.maxDurationInMilliseconds
					: 100);
			seekBar.setProgress(bundle.maxDurationInMilliseconds != null
					? bundle.currentPositionInMilliseconds
					: 0);
		});
	}

	@Override
	protected void onStop() {
		this.playback.setPlaybackListener(null);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		this.playback.release();
		super.onDestroy();
	}
}
