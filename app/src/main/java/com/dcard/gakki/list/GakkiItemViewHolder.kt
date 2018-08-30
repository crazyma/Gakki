package com.dcard.gakki.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dcard.gakki.R
import com.dcard.gakki.api.PostModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_gakki_list.view.*

class GakkiItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup) =
                GakkiItemViewHolder(inflater.inflate(
                        R.layout.item_gakki_list, parent, false))
    }

    fun bindTo(data: PostModel){
        itemView.titleTextView.text = data.title
        itemView.contentTextView.text = data.content
        Picasso.get()
                .load(data.thumbnail)
                .fit()
                .into(itemView.thumbnailImageView)
    }
}