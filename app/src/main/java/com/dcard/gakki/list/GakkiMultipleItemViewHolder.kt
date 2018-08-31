package com.dcard.gakki.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dcard.gakki.R
import com.dcard.gakki.api.PostModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_gakki_list_multiple.view.*

class GakkiMultipleItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup) =
                GakkiMultipleItemViewHolder(inflater.inflate(
                        R.layout.item_gakki_list_multiple, parent, false))
    }

    fun bindTo(data: PostModel){
        itemView.titleTextView.text = data.title
        itemView.contentTextView.text = data.content

        if(data.thumbnail ==  null || data.thumbnail!!.isEmpty()){
            itemView.cardView.visibility = View.GONE
        }else {
            itemView.cardView.visibility = View.VISIBLE
            Picasso.get()
                    .load(data.thumbnail)
                    .placeholder(R.drawable.background_gray)
                    .fit()
                    .centerCrop()
                    .into(itemView.thumbnailImageView)
        }
    }
}