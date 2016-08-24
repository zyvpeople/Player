package com.develop.zuzik.musicbrowser.presentation.fragment.playback

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.develop.zuzik.musicbrowser.R
import com.develop.zuzik.musicbrowser.domain.entity.Song
import com.develop.zuzik.musicbrowser.presentation.adapter.PlaybackListRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_playback_list.*

/**
 * User: zuzik
 * Date: 8/21/16
 */
class PlaybackListFragment : Fragment() {

    companion object {
        fun create(): PlaybackListFragment = PlaybackListFragment()
    }

    val adapter = PlaybackListRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.songs = listOf(Song("author", "name", Uri.parse("")))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragment_playback_list, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }
}