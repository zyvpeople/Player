package com.develop.zuzik.audioplayerexample;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.develop.zuzik.audioplayerexample.player.Playback;
import com.develop.zuzik.audioplayerexample.player.exceptions.CreatePlayerException;
import com.develop.zuzik.audioplayerexample.player.exceptions.PlayerAlreadyInitializedException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Playback playback = new Playback();
		try {
			playback.initWithRawResource(this, R.raw.song);
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
				playback.start();
			}
		});
		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.pause();
			}
		});
		stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playback.stop();
			}
		});
	}
}
