package com.develop.zuzik.audioplayerexample.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerVideoPresenter;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.video.Listener;
import com.develop.zuzik.player.video.SurfaceViewWrapper;

public class VideoActivity extends AppCompatActivity implements MultiplePlayer.VideoView<Song> {

	public static Intent createIntent(Context context, Song song) {
		return new Intent(context, VideoActivity.class)
				.putExtra("song", song);
	}

	private static void parseIntent(Intent intent, ParamAction<Song> success) {
		success.execute((Song) intent.getSerializableExtra("song"));
	}

	private SurfaceView surfaceView;
	private SurfaceViewWrapper surfaceViewWrapper;
	private MultiplePlayer.VideoPresenter<Song> presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		parseIntent(getIntent(), value -> this.presenter = new MultiplePlayerVideoPresenter<Song>(getModel(), value));
		this.surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		this.surfaceViewWrapper = new SurfaceViewWrapper(
				this.surfaceView,
				new Listener() {
					@Override
					public void onCreated() {
						presenter.onVideoViewCreated();
					}

					@Override
					public void onDestroyed() {
						presenter.onVideoViewDestroyed();
					}
				});
		this.presenter.setView(this);
		this.presenter.onCreate();
	}

	@Override
	protected void onDestroy() {
		this.presenter.onDestroy();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.presenter.onAppear();
	}

	@Override
	protected void onStop() {
		this.presenter.onDisappear();
		super.onStop();
	}

	private MultiplePlayer.Model<Song> getModel() {
		return ((App) getApplicationContext()).getMultiplePlayerModel();
	}

	//region MultiplePlayer.VideoView

	@Override
	public void setVideoViewAvailable() {
	}

	@Override
	public void setVideoViewUnavailable() {

	}

	@Override
	public void setVideoView(VideoViewSetter setter) {
		this.surfaceViewWrapper.setVideoView(setter);
	}

	@Override
	public void clearVideoView(VideoViewSetter setter) {
		this.surfaceViewWrapper.clearVideoView(setter);
	}

	//endregion
}
