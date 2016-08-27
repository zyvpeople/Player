package com.develop.zuzik.audioplayerexample.presentation.fragments;


import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.application.App;
import com.develop.zuzik.audioplayerexample.domain.Song;
import com.develop.zuzik.audioplayerexample.presentation.activities.MultipleVideoActivity;
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerActiveSourcePresenter;
import com.develop.zuzik.multipleplayermvp.presenter.MultiplePlayerVideoPresenter;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.develop.zuzik.player.interfaces.VideoViewSetter;
import com.develop.zuzik.player.source.PlayerSource;
import com.develop.zuzik.player.video.Listener;
import com.develop.zuzik.player.video.SurfaceViewWrapper;
import com.squareup.picasso.Picasso;

public class SongFragment extends Fragment implements MultiplePlayer.VideoView<Song>, MultiplePlayer.ActiveSourceView<Song> {

	private static final String ARGUMENT_SONG = "ARGUMENT_SONG";

	public static SongFragment newInstance(PlayerSource<Song> song) {
		SongFragment fragment = new SongFragment();
		Bundle args = new Bundle();
		args.putSerializable(ARGUMENT_SONG, song);
		fragment.setArguments(args);
		return fragment;
	}

	private static void parseArguments(Bundle arguments, ParamAction<PlayerSource<Song>> success) {
		success.execute((PlayerSource<Song>) arguments.getSerializable(ARGUMENT_SONG));
	}

	private ViewGroup content;
	private ImageView image;
	private SurfaceView surfaceView;
	private SurfaceViewWrapper surfaceViewWrapper;
	private MultiplePlayer.VideoPresenter<Song> presenter;
	private MultiplePlayer.ActiveSourcePresenter<Song> activeSourcePresenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_song, container, false);
		this.content = (ViewGroup) view.findViewById(R.id.content);
		this.image = (ImageView) view.findViewById(R.id.image);
		this.surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
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
		parseArguments(getArguments(), new ParamAction<PlayerSource<Song>>() {
			@Override
			public void execute(final PlayerSource<Song> value) {
				Picasso
						.with(SongFragment.this.getContext())
						.load(value.getSourceInfo().image)
						.into(SongFragment.this.image);
				SongFragment.this.presenter = new MultiplePlayerVideoPresenter<>(SongFragment.this.getModel(), value.getSourceInfo());
				SongFragment.this.activeSourcePresenter = new MultiplePlayerActiveSourcePresenter<Song>(SongFragment.this.getModel());
				SongFragment.this.activeSourcePresenter.setPlayerSource(value);
				SongFragment.this.surfaceView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						SongFragment.this.startActivity(MultipleVideoActivity.createIntent(getContext(), value.getSourceInfo()));
					}
				});
			}
		});
		this.presenter.setView(this);
		this.activeSourcePresenter.setView(this);

		this.presenter.onCreate();
		this.activeSourcePresenter.onCreate();

		return view;
	}

	@Override
	public void onDestroyView() {
		this.presenter.onDestroy();
		this.activeSourcePresenter.onDestroy();
		super.onDestroyView();
	}

	@Override
	public void onStart() {
		super.onStart();
		this.presenter.onAppear();
		this.activeSourcePresenter.onAppear();
	}

	@Override
	public void onStop() {
		this.presenter.onDisappear();
		this.activeSourcePresenter.onDisappear();
		super.onStop();
	}

	private MultiplePlayer.Model<Song> getModel() {
		return ((App) getActivity().getApplicationContext()).getMultiplePlayerModel();
	}

	//region MultiplePlayer.VideoView

	@Override
	public void setVideoViewAvailable() {
		this.surfaceView.setVisibility(View.VISIBLE);
	}

	@Override
	public void setVideoViewUnavailable() {
		this.surfaceView.setVisibility(View.GONE);
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

	//region MultiplePlayer.ActiveSourceView<Song>

	@Override
	public void displayAsActiveSource() {
		this.content.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.active));
	}

	@Override
	public void displayAsInactiveSource() {
		this.content.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.inactive));
	}

	//endregion

}
