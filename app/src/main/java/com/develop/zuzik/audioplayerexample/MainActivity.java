package com.develop.zuzik.audioplayerexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.develop.zuzik.audioplayerexample.player.Playback;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Playback playback = new Playback(R.raw.song_short);
//		final Playback playback = new Playback(R.raw.song);
//		final Playback playback = new Playback(Uri.parse("http://storage.mp3.cc/download/454079/dG5Dd2NNMy8vZ1NUc2hINFZtRXl4OUt4c2RjZXhvdmkra3liTmFnOTFWMlZibUlCMlZRTXcwcVVhckszaldDSGRqMzRLaTg2ckpkQVhxZHYya3NKc09MM0VvNnFFQ2g3ZnNUYTlMS3M2YlY5MkhtcEpYTlR4V1JPaUJUcHhWMU8/Of_Monsters_And_Men-Little_Talks_(mp3.cc).mp3"));

		findViewById(R.id.play).setOnClickListener(v -> playback.play(MainActivity.this));
		findViewById(R.id.pause).setOnClickListener(v -> playback.pause());
		findViewById(R.id.stop).setOnClickListener(v -> playback.stop());
	}
}
