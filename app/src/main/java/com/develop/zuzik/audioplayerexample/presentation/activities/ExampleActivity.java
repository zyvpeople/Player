package com.develop.zuzik.audioplayerexample.presentation.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.audioplayerexample.mvp.implementations.models.PlayerModel;
import com.develop.zuzik.audioplayerexample.mvp.intarfaces.Player;
import com.develop.zuzik.audioplayerexample.presentation.fragments.ExampleFragment;

public class ExampleActivity extends AppCompatActivity implements ExampleFragment.OnFragmentInteractionListener {

	private PlayerModel<Song> model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);
		this.model = new PlayerModel<>(this);
	}

	@Override
	public Player.Model<Song> getModel() {
		return this.model;
	}
}