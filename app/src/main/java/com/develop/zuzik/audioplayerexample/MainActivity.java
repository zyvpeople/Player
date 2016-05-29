package com.develop.zuzik.audioplayerexample;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.develop.zuzik.audioplayerexample.player.Playback;
import com.develop.zuzik.audioplayerexample.player.exceptions.CreatePlayerException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerAlreadyInitializedException;
import com.develop.zuzik.audioplayerexample.player.exceptions.UnknownPlayerSourceException;
import com.develop.zuzik.audioplayerexample.player.player_sources.UriPlayerSource;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Playback playback = new Playback();
		try {
//			playback.initWithPlayerSource(this, new RawResourcePlayerSource(R.raw.song));
			playback.initWithPlayerSource(this, new UriPlayerSource(Uri.parse("http://storage.mp3.cc/download/454079/dG5Dd2NNMy8vZ1NUc2hINFZtRXl4OUt4c2RjZXhvdmkra3liTmFnOTFWMlZibUlCMlZRTXcwcVVhckszaldDSGRqMzRLaTg2ckpkQVhxZHYya3NKc09MM0VvNnFFQ2g3ZnNUYTlMS3M2YlY5MkhtcEpYTlR4V1JPaUJUcHhWMU8/Of_Monsters_And_Men-Little_Talks_(mp3.cc).mp3")));
		} catch (UnknownPlayerSourceException e) {
			e.printStackTrace();
		} catch (PlayerAlreadyInitializedException e) {
			e.printStackTrace();
		} catch (CreatePlayerException e) {
			e.printStackTrace();
		}

		Button play = (Button) findViewById(R.id.play);
		Button stop = (Button) findViewById(R.id.stop);
		Button pause = (Button) findViewById(R.id.pause);
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.start(MainActivity.this);
			}
		});
		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.pause(MainActivity.this);
			}
		});
		stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.stop(MainActivity.this);
			}
		});
	}
}
