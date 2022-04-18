package com.spacex.launches.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class SimpleViewHolder(
    view: View
): RecyclerView.ViewHolder(view) {

    protected var dataPosition: Int = -1
        private set

    fun applyData(position: Int) {
        dataPosition = position
        refresh(position)
    }

    protected open fun refresh(position: Int) {

    }


}
