package com.develop.zuzik.musicbrowser.presentation.fragment.playback

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.develop.zuzik.multipleplayermvp.interfaces.MultiplePlayer
import com.develop.zuzik.musicbrowser.R
import com.develop.zuzik.musicbrowser.application.injection.injector.PlaybackListFragmentInjector
import com.develop.zuzik.musicbrowser.domain.entity.Song
import com.develop.zuzik.musicbrowser.presentation.adapter.PlaybackListRecyclerViewAdapter
import com.develop.zuzik.player.source.PlayerSource
import com.develop.zuzik.player.source.UriPlayerSource
import kotlinx.android.synthetic.main.fragment_playback_list.*
import javax.inject.Inject

/**
 * User: zuzik
 * Date: 8/21/16
 */
class PlaybackListFragment : Fragment(), MultiplePlayer.View<Song> {

    companion object {
        fun create(): PlaybackListFragment = PlaybackListFragment()
    }

    @Inject
    lateinit var presenter: MultiplePlayer.Presenter<Song>
    @Inject
    lateinit var controlPresenter: MultiplePlayer.ControlPresenter<Song>

    val adapter = PlaybackListRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlaybackListFragmentInjector().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragment_playback_list, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        this.presenter.setView(this)
        this.presenter.onCreate()
        this.controlPresenter.onCreate()
        this.presenter.onSetPlayerSources(listOf(
                UriPlayerSource(Song("Enter Shikari", "the Last garrison", ""), "http://www.ex.ua/get/147185586"),
                UriPlayerSource(Song("Enter Shikari", "the Last garrison", ""), "http://www.ex.ua/get/147185586")))
//        this.presenter.onSetPlayerSources(listOf())
    }

    override fun onDestroyView() {
        this.presenter.onDestroy()
        this.presenter.setView(null)
        this.controlPresenter.onDestroy()
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        this.presenter.onAppear()
        this.controlPresenter.onPlay()
    }

    override fun onStop() {
        this.presenter.onDisappear()
        this.controlPresenter.onDisappear()
        super.onStop()
    }

    //region MultiplePlayer.View<Song>

    override fun showError(message: String) {
    }

    override fun displayCurrentSource(source: PlayerSource<Song>) {
    }

    override fun doNotDisplayCurrentSource() {
    }

    override fun displaySources(playerSources: MutableList<PlayerSource<Song>>) {
        if (this.adapter.songs.equals(playerSources)) {
            return
        }
        this.adapter.songs = playerSources
        this.adapter.notifyDataSetChanged()
    }

    //endregion
}