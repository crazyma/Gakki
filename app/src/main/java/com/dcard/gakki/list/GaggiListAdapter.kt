package com.dcard.gakki.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dcard.gakki.api.PostModel

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

    var dataList: List<PostModel>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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