package com.develop.zuzik.audioplayerexample.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.audioplayerexample.presentation.fragments.PlayerFragment;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerHidingPresenter;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerActivity extends AppCompatActivity implements MultiplePlayer.HidingView<Song> {

	private MultiplePlayer.HidingPresenter<Song> hidingPresenter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);

		this.hidingPresenter = new MultiplePlayerHidingPresenter<>(getModel());
		this.hidingPresenter.setView(this);

		findViewById(R.id.addLeft).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = PlayerActivity.this.getSupportFragmentManager().findFragmentById(R.id.leftContainer);
				if (fragment == null) {
					fragment = new PlayerFragment();
					PlayerActivity.this.getSupportFragmentManager()
							.beginTransaction()
							.add(R.id.leftContainer, fragment)
							.commit();
				}
			}
		});
		findViewById(R.id.removeLeft).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = PlayerActivity.this.getSupportFragmentManager().findFragmentById(R.id.leftContainer);
				if (fragment != null) {
					PlayerActivity.this.getSupportFragmentManager()
							.beginTransaction()
							.remove(fragment)
							.commit();
				}
			}
		});

		findViewById(R.id.addRight).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addRight();
			}
		});
		findViewById(R.id.removeRight).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				removeRight();
			}
		});
		this.hidingPresenter.onCreate();
	}

	private void addRight() {
		Fragment fragment = PlayerActivity.this.getSupportFragmentManager().findFragmentById(R.id.rightContainer);
		if (fragment == null) {
			fragment = new PlayerFragment();
			PlayerActivity.this.getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.rightContainer, fragment)
					.commit();
		}
	}

	private void removeRight() {
		Fragment fragment = PlayerActivity.this.getSupportFragmentManager().findFragmentById(R.id.rightContainer);
		if (fragment != null) {
			PlayerActivity.this.getSupportFragmentManager()
					.beginTransaction()
					.remove(fragment)
					.commit();
		}
	}

	@Override
	protected void onDestroy() {
		this.hidingPresenter.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.hidingPresenter.onAppear();
	}

	@Override
	protected void onStop() {
		this.hidingPresenter.onDisappear();
		super.onStop();
	}

	private MultiplePlayer.Model<Song> getModel() {
		return ((App) getApplicationContext()).getMultiplePlayerModel();
	}

	//region MultiplePlayer.HidingView<Song>

	@Override
	public void displayPlayerView() {
		addRight();
	}

	@Override
	public void doNotDisplayPlayerView() {
		removeRight();
	}

	//endregion
}
