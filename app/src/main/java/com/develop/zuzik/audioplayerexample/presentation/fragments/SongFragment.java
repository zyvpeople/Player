package com.develop.zuzik.audioplayerexample.presentation.fragments;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.audioplayerexample.presentation.player_exception_message_provider.ExamplePlayerExceptionMessageProvider;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerPresenter;
import com.develop.zuzik.multipleplayermvp.presenter_destroy_strategy.DoNothingMultiplePlayerPresenterDestroyStrategy;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.player.video.Listener;
import com.develop.zuzik.player.video.SurfaceViewWrapper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongFragment extends Fragment implements MultiplePlayer.View<Song> {

	private static final String ARGUMENT_SONG = "ARGUMENT_SONG";

	public static SongFragment newInstance(Song song) {
		SongFragment fragment = new SongFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARGUMENT_SONG, song);
		fragment.setArguments(args);
		return fragment;
	}

	private static void parseArguments(Bundle arguments, ParamAction<Song> success) {
		success.execute((Song) arguments.getSerializable(ARGUMENT_SONG));
	}

	private ImageView image;
	private SurfaceViewWrapper surfaceViewWrapper;
	private MultiplePlayer.Presenter<Song> presenter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.presenter = new MultiplePlayerPresenter<>(
				getModel(),
				new DoNothingMultiplePlayerPresenterDestroyStrategy(),
				new ExamplePlayerExceptionMessageProvider());
		this.presenter.setView(this);
		this.presenter.onCreate();
	}

	@Override
	public void onDestroy() {
		this.presenter.onDestroy();
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_song, container, false);
		this.image = (ImageView) view.findViewById(R.id.image);
		this.surfaceViewWrapper = new SurfaceViewWrapper(
				(SurfaceView) view.findViewById(R.id.surfaceView),
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
		parseArguments(getArguments(), value -> Picasso
				.with(getContext())
				.load(value.image)
				.into(this.image));
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		this.presenter.onAppear();
	}

	@Override
	public void onStop() {
		this.presenter.onDisappear();
		super.onStop();
	}

	private MultiplePlayer.Model<Song> getModel() {
		return ((App) getActivity().getApplicationContext()).getMultiplePlayerModel();
	}

	//region MultiplePlayer.View

	@Override
	public void repeat() {

	}

	@Override
	public void doNotRepeat() {

	}

	@Override
	public void shuffle() {

	}

	@Override
	public void doNotShuffle() {

	}

	@Override
	public void setProgress(int currentTimeInMilliseconds, int totalTimeInMilliseconds) {

	}

	@Override
	public void showProgress() {

	}

	@Override
	public void hideProgress() {

	}

	@Override
	public void showTime(String currentTime, String totalTime) {

	}

	@Override
	public void showError(String message) {

	}

	@Override
	public void enablePlayControls(boolean play, boolean pause, boolean stop) {

	}

	@Override
	public void displayCurrentSource(PlayerSource<Song> source) {

	}

	@Override
	public void doNotDisplayCurrentSource() {

	}

	@Override
	public void displaySources(List<PlayerSource<Song>> playerSources) {

	}

	@Override
	public void setVideoView(MediaPlayer player) {
		this.surfaceViewWrapper.setVideoView(player);
	}

	@Override
	public void clearVideoView(MediaPlayer player) {
		this.surfaceViewWrapper.clearVideoView(player);
	}


	//endregion
}
