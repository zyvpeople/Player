package com.develop.zuzik.multipleplayermvp.presenter;

import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zuzik
 * Date: 8/28/16
 */
public class CompositeMultiplePlayerBasePresenter<SourceInfo> implements MultiplePlayer.BasePresenter<SourceInfo> {

	private final List<MultiplePlayer.BasePresenter<SourceInfo>> presenters = new ArrayList<>();

	public void add(MultiplePlayer.BasePresenter<SourceInfo> presenter) {
		if (!this.presenters.contains(presenter)) {
			this.presenters.add(presenter);
		}
	}

	public void remove(MultiplePlayer.BasePresenter<SourceInfo> presenter) {
		this.presenters.remove(presenter);
	}

	@Override
	public void onCreate() {
		for (MultiplePlayer.BasePresenter<SourceInfo> presenter : this.presenters) {
			presenter.onCreate();
		}
	}

	@Override
	public void onDestroy() {
		for (MultiplePlayer.BasePresenter<SourceInfo> presenter : this.presenters) {
			presenter.onDestroy();
		}
	}

	@Override
	public void onAppear() {
		for (MultiplePlayer.BasePresenter<SourceInfo> presenter : this.presenters) {
			presenter.onAppear();
		}
	}

	@Override
	public void onDisappear() {
		for (MultiplePlayer.BasePresenter<SourceInfo> presenter : this.presenters) {
			presenter.onDisappear();
		}
	}
}
