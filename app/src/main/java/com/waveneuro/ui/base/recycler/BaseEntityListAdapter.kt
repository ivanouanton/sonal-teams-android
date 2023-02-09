package com.waveneuro.ui.base.recycler

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseEntityListAdapter<E, VH : EntityVH<E>>(
    context: Context,
    diffCallback: DiffUtil.ItemCallback<E>
) : ListAdapter<E, VH>(diffCallback) {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bind(getItem(position), payloads)
        }
    }

    public override fun getItem(position: Int): E = super.getItem(position)

}
