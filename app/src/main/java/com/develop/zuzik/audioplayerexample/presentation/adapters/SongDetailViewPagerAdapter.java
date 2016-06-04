package com.develop.zuzik.audioplayerexample.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.develop.zuzik.audioplayerexample.presentation.fragments.SongDetailFragment;

/**
 * User: zuzik
 * Date: 6/4/16
 */
public class SongDetailViewPagerAdapter extends FragmentStatePagerAdapter {

	private final int songsCount;

	public SongDetailViewPagerAdapter(FragmentManager fm, int songsCount) {
		super(fm);
		this.songsCount = songsCount;
	}

	@Override
	public Fragment getItem(int position) {
		return SongDetailFragment.newInstance();
	}

	@Override
	public int getCount() {
		return this.songsCount;
	}
}
