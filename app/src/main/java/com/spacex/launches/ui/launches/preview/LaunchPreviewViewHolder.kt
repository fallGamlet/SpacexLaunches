package com.spacex.launches.ui.launches.preview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.spacex.launches.R
import com.spacex.launches.ui.adapters.SimpleViewHolder
import java.util.*

class LaunchPreviewViewHolder(
    view: View,
    private val provideData: (Int) -> LaunchPreviewsViewState.PreviewItem,
    private val formatDate: (Date) -> String,
    private val onClickListener: (Int) -> Unit,
    private val onFavoriteToggleClickListener: (Int) -> Unit
): SimpleViewHolder(view) {

    private val imageView: ImageView = view.findViewById(R.id.imageView)
    private val titleView: TextView = view.findViewById(R.id.titleView)
    private val subtitleView: TextView = view.findViewById(R.id.subtitleView)
    private val favoriteView: MaterialButton = view.findViewById(R.id.favoriteView)

    init {
        view.setOnClickListener { onClickListener(dataPosition) }
        favoriteView.setOnClickListener { onFavoriteToggleClickListener(dataPosition) }
    }

    override fun refresh(position: Int) {
        super.refresh(position)
        val data = provideData(position)
        val subtitle = formatDate(data.date)

        titleView.text = data.title
        titleView.isVisible = data.title.isNotBlank()
        subtitleView.text = subtitle
        subtitleView.isVisible = subtitle.isNotBlank()
        favoriteView.isChecked = data.isFavorite

        if (data.imageUrl.isBlank()) {
            imageView.setImageResource(R.drawable.shape_image_dummy)
        } else {
            Glide.with(imageView)
                .load(data.imageUrl)
                .error(R.drawable.shape_image_dummy)
                .fitCenter()
                .into(imageView)
        }
    }

}
