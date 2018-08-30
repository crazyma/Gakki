package com.dcard.gakki.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class GaggiListAdapter(

        private val context: Context

): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, id: Int)
    }

    companion object {
        const val ITEM_TYPE_ROW = 0x01
        const val ITEM_TYPE_TITLE = 0x02
    }

    var dataList: List<String>? = listOf("111","222","333","444","555","666","777","111","222","333","444","555","666","777")

    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)

        return GakkiItemViewHolder.create(layoutInflater, parent)
    }

    override fun getItemCount() = dataList?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val data = dataList!![position]
        when (holder) {
            is GakkiItemViewHolder -> {
                holder.bindTo(data)
            }

        }
    }

}