package com.develop.zuzik.audioplayerexample.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
				Fragment fragment = PlayerActivity.this.getSupportFragmentManager().findFragmentById(R.id.rightContainer);
				if (fragment == null) {
					fragment = new PlayerFragment();
					PlayerActivity.this.getSupportFragmentManager()
							.beginTransaction()
							.add(R.id.rightContainer, fragment)
							.commit();
				}
			}
		});
		findViewById(R.id.removeRight).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = PlayerActivity.this.getSupportFragmentManager().findFragmentById(R.id.rightContainer);
				if (fragment != null) {
					PlayerActivity.this.getSupportFragmentManager()
							.beginTransaction()
							.remove(fragment)
							.commit();
				}
			}
		});
	}
}
