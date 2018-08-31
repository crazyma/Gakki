package com.dcard.gakki.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dcard.gakki.R
import com.dcard.gakki.api.PostModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_gakki_list_single.view.*

class GakkiSingleItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup) =
                GakkiSingleItemViewHolder(inflater.inflate(
                        R.layout.item_gakki_list_single, parent, false))
    }

    fun bindTo(data: PostModel){
        itemView.singleTitleTextView.text = data.title
        itemView.singleContentTextView.text = data.content

        if(data.thumbnail == null || data.thumbnail!!.isEmpty()){
            itemView.singleCardView.visibility = View.GONE
        }else{
            itemView.singleCardView.visibility = View.VISIBLE

            Picasso.get()
                    .load(data.thumbnail)
                    .placeholder(R.drawable.background_gray)
                    .fit()
                    .centerCrop()
                    .into(itemView.singlePhotoImageView)
        }
    }
}