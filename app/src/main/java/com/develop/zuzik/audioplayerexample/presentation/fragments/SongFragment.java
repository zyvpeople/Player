package com.develop.zuzik.audioplayerexample.presentation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.develop.zuzik.audioplayerexample.R;
import com.develop.zuzik.audioplayerexample.entities.Song;
import com.develop.zuzik.player.interfaces.ParamAction;
import com.squareup.picasso.Picasso;

public class SongFragment extends Fragment {

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_song, container, false);
		this.image = (ImageView) view.findViewById(R.id.image);
		parseArguments(getArguments(), value -> Picasso
				.with(getContext())
				.load(value.image)
				.into(this.image));
		return view;
	}

}
