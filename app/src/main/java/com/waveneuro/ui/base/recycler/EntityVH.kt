package com.waveneuro.ui.base.recycler

import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

abstract class EntityVH<E>(view: View) : RecyclerView.ViewHolder(view) {

    @CallSuper
    open fun bind(model: E) {
    }

    @CallSuper
    open fun bind(model: E, payloads: MutableList<Any>) {
    }

}
