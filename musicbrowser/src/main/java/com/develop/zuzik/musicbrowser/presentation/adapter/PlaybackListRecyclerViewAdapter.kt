package com.develop.zuzik.musicbrowser.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.develop.zuzik.musicbrowser.domain.entity.Song
import com.develop.zuzik.musicbrowser.presentation.view.PlaybackListItemView
import com.develop.zuzik.musicbrowser.presentation.view_holder.ViewHolder
import com.develop.zuzik.player.source.PlayerSource

/**
 * User: zuzik
 * Date: 8/21/16
 */
class PlaybackListRecyclerViewAdapter : RecyclerView.Adapter<ViewHolder<PlaybackListItemView>>() {

    var songs: List<PlayerSource<Song>> = emptyList()

    override fun getItemCount(): Int = this.songs.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder<PlaybackListItemView> {
        val view = PlaybackListItemView(parent?.context)
        view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder<PlaybackListItemView>?, position: Int) {
        holder?.view?.song = this.songs[position]
    }
}