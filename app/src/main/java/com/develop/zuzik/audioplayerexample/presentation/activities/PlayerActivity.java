package com.develop.zuzik.audioplayerexample.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.presentation.fragments.PlayerFragment;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class PlayerActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);

		findViewById(R.id.addLeft).setOnClickListener(v -> {
			Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.leftContainer);
			if (fragment == null) {
				fragment = new PlayerFragment();
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
				fragment = new PlayerFragment();
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
	}
}
