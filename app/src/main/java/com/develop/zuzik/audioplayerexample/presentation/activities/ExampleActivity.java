package com.develop.zuzik.audioplayerexample.presentation.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.mvp.implementations.models.PlayerModel;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.player.playback.local.LocalPlaybackFactory;
import com.develop.zuzik.audioplayerexample.player.playback.settings.InMemoryPlaybackSettings;
import com.develop.zuzik.audioplayerexample.presentation.fragments.ExampleFragment;

public class ExampleActivity extends AppCompatActivity implements ExampleFragment.OnFragmentInteractionListener {

	private PlayerModel<Song> model;

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

		this.model = new PlayerModel<>(this, new InMemoryPlaybackSettings(), new LocalPlaybackFactory<>());
	}

	@Override
	public Player.Model<Song> getModel() {
		return this.model;
	}
}