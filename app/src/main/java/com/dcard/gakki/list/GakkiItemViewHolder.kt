package com.dcard.gakki.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dcard.gakki.R
import kotlinx.android.synthetic.main.item_gakki_list.view.*

class GakkiItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup) =
                GakkiItemViewHolder(inflater.inflate(
                        R.layout.item_gakki_list, parent, false))
    }

    fun bindTo(text: String){
        itemView.titleTextView.text = text
    }
}