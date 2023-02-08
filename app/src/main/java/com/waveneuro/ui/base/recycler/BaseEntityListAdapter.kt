package com.waveneuro.ui.base.recycler

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseEntityListAdapter<E, VH : EntityVH<E>>(
    context: Context,
    diffCallback: DiffUtil.ItemCallback<E>
) : ListAdapter<E, VH>(diffCallback) {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    private var submitDelayedJob: Job? = null

    //region inherited

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

    //endregion

    //region own methods

    fun submitDelayed(
        coroutineScope: CoroutineScope,
        list: List<E>,
        preSubmitAction: (() -> Unit)? = null,
        delayMs: Long = 100
    ) {
        submitDelayedJob?.cancel()
        submitDelayedJob = coroutineScope.launch {
            delay(delayMs)
            preSubmitAction?.invoke()
            submitList(list)
        }
    }

    fun submitDelayed(
        coroutineScope: CoroutineScope,
        list: List<E>,
        preSubmitAction: (() -> Unit)? = null,
        delayMs: Long = 100,
        commitCallback: () -> Unit
    ) {
        submitDelayedJob?.cancel()
        submitDelayedJob = coroutineScope.launch {
            delay(delayMs)
            preSubmitAction?.invoke()
            submitList(list, commitCallback)
        }
    }

    //endregion

}
