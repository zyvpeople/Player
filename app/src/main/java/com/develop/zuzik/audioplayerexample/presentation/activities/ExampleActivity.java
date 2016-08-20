package com.develop.zuzik.audioplayerexample.presentation.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.presentation.fragments.ExampleFragment;

public class ExampleActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_example);

		findViewById(R.id.addLeft).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = ExampleActivity.this.getSupportFragmentManager().findFragmentById(R.id.leftContainer);
				if (fragment == null) {
					fragment = new ExampleFragment();
					ExampleActivity.this.getSupportFragmentManager()
							.beginTransaction()
							.add(R.id.leftContainer, fragment)
							.commit();
				}
			}
		});
		findViewById(R.id.removeLeft).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = ExampleActivity.this.getSupportFragmentManager().findFragmentById(R.id.leftContainer);
				if (fragment != null) {
					ExampleActivity.this.getSupportFragmentManager()
							.beginTransaction()
							.remove(fragment)
							.commit();
				}
			}
		});

		findViewById(R.id.addRight).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = ExampleActivity.this.getSupportFragmentManager().findFragmentById(R.id.rightContainer);
				if (fragment == null) {
					fragment = new ExampleFragment();
					ExampleActivity.this.getSupportFragmentManager()
							.beginTransaction()
							.add(R.id.rightContainer, fragment)
							.commit();
				}
			}
		});
		findViewById(R.id.removeRight).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = ExampleActivity.this.getSupportFragmentManager().findFragmentById(R.id.rightContainer);
				if (fragment != null) {
					ExampleActivity.this.getSupportFragmentManager()
							.beginTransaction()
							.remove(fragment)
							.commit();
				}
			}
		});
	}
}