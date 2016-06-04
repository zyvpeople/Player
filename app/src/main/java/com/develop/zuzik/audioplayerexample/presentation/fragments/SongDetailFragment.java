package com.develop.zuzik.audioplayerexample.presentation.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.develop.zuzik.audioplayerexample.R;

public class SongDetailFragment extends Fragment {

	public static SongDetailFragment newInstance() {
		SongDetailFragment fragment = new SongDetailFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_song_detail, container, false);
	}

}
