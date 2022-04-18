package com.spacex.launches.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SimpleRecyclerAdapter(
    private val provideCount: () -> Int,
    private val provideHolder: (parent: ViewGroup, viewType: Int) -> SimpleViewHolder,
    private val provideViewType: ((Int) -> Int)? = null
): RecyclerView.Adapter<SimpleViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return provideViewType?.invoke(position)
            ?: super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        return provideHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.applyData(position)
    }

    override fun getItemCount(): Int = provideCount()

}
