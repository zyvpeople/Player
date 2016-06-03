package com.develop.zuzik.audioplayerexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.develop.zuzik.audioplayerexample.player.Playback;

public class MainActivity extends AppCompatActivity {

	Playback playback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		playback = new Playback(R.raw.song_short);
		playback = new Playback(R.raw.song);
//		playback = new Playback(Uri.parse("http://storage.mp3.cc/download/454079/dG5Dd2NNMy8vZ1NUc2hINFZtRXl4OUt4c2RjZXhvdmkra3liTmFnOTFWMlZibUlCMlZRTXcwcVVhckszaldDSGRqMzRLaTg2ckpkQVhxZHYya3NKc09MM0VvNnFFQ2g3ZnNUYTlMS3M2YlY5MkhtcEpYTlR4V1JPaUJUcHhWMU8/Of_Monsters_And_Men-Little_Talks_(mp3.cc).mp3"));

		findViewById(R.id.play).setOnClickListener(v -> playback.play(MainActivity.this));
		findViewById(R.id.pause).setOnClickListener(v -> playback.pause());
		findViewById(R.id.stop).setOnClickListener(v -> playback.stop());
		findViewById(R.id.fakeError).setOnClickListener(v -> playback.fakeError());
		SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

		TextView maxDuration = (TextView) findViewById(R.id.maxDuration);
		TextView currentPosition = (TextView) findViewById(R.id.currentPosition);

		playback.setPlaybackListener(bundle -> {
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

		playback.init();
	}

	@Override
	protected void onStop() {
		playback.release();
		super.onStop();
	}
}
