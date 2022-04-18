package com.spacex.launches.ui.launches.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.spacex.launches.R
import com.spacex.launches.getRepositoryProvider
import com.spacex.launches.openUrl
import java.text.SimpleDateFormat
import java.util.*

class LaunchesDetailsFragment :
    Fragment()
{

    private var youtubePlayerView: YouTubePlayerView? = null
    private var favoriteView: MaterialButton? = null
    private var wikipediaLinkView: View? = null
    private var titleView: TextView? = null
    private var dateView: TextView? = null
    private var rocketView: TextView? = null
    private var payloadView: TextView? = null
    private var descriptionView: TextView? = null
    private var loadingView: View? = null
    private var youTubePlayer: YouTubePlayer? = null
    private var youTubePlayerVideoPrepared: Boolean = false
    private var viewState: LaunchDetailsViewState = LaunchDetailsViewState.Idle()
    private val dateFormatter =
        SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault())

    private val viewModel: LaunchesDetailsViewModel by viewModels {
        val repository = getRepositoryProvider()
            .getLaunchRepository()
        LaunchesDetailsViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launchId = arguments?.getString(ARG_LAUNCH_ID) ?: ""
        viewModel.setData(launchId)

        lifecycleScope.launchWhenResumed {
            viewModel.viewState.collect {
                updateState(it)
            }
        }
    }

    private fun updateState(state: LaunchDetailsViewState) {
        viewState = state
        refreshData()
        loadingView?.isVisible = false
        when (state) {
            is LaunchDetailsViewState.Idle -> { }
            is LaunchDetailsViewState.Loading -> {
                loadingView?.isVisible = true
            }
            is LaunchDetailsViewState.Success -> { }
            is LaunchDetailsViewState.Error -> {
                Snackbar.make(
                    requireView(),
                    state.exception.message ?: "Error",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun refreshData() {
        val data = viewState.data

        titleView?.text = data.title

        dateView?.text = formatData(data.date)
        dateView?.isVisible = data.date.time != 0L

        rocketView?.text = data.rocketName
        rocketView?.isVisible = data.rocketName.isNotBlank()

        payloadView?.text = "Payload mass ${data.payloadMass} Kg"
        payloadView?.isVisible = data.payloadMass.toFloat() > 0f

        wikipediaLinkView?.isVisible = data.wikipediaUrl.isNotBlank()

        descriptionView?.text = data.description
        descriptionView?.isVisible = data.description.isNotBlank()

        favoriteView?.isChecked = data.isFavorite

        youtubePlayerView?.isVisible = data.youtubeId.isNotBlank()
        val canInitYoutube = youTubePlayer != null &&
                !youTubePlayerVideoPrepared &&
                data.youtubeId.isNotBlank()
        if (canInitYoutube) {
            youTubePlayerVideoPrepared = true
            youTubePlayer?.cueVideo(data.youtubeId, 0f)
        }
    }

    private fun formatData(date: Date): String {
        return if (date.time == 0L) ""
        else  dateFormatter.format(date)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        youtubePlayerView = null
        favoriteView = null
        wikipediaLinkView = null
        titleView = null
        dateView = null
        rocketView = null
        payloadView = null
        descriptionView = null
        loadingView = null
        youTubePlayer = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.screen_launch_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initYoutubePlayer()
        initViewListeners()
    }

    private fun initViews(view: View) {
        youtubePlayerView = view.findViewById(R.id.youtubePlayerView)
        favoriteView = view.findViewById(R.id.favoriteView)
        wikipediaLinkView = view.findViewById(R.id.wikipediaLinkView)
        titleView = view.findViewById(R.id.titleView)
        dateView = view.findViewById(R.id.dateView)
        rocketView = view.findViewById(R.id.rocketView)
        payloadView = view.findViewById(R.id.payloadView)
        descriptionView = view.findViewById(R.id.descriptionView)
        loadingView = view.findViewById(R.id.loadingView)
    }

    private fun initYoutubePlayer() {
        val youtubePlayerView = youtubePlayerView ?: return
        lifecycle.addObserver(youtubePlayerView)
        youtubePlayerView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                this@LaunchesDetailsFragment.youTubePlayer = youTubePlayer
                val youtubeId = viewState.data.youtubeId
                if (youtubeId.isNotBlank()) {
                    youTubePlayerVideoPrepared = true
                    youTubePlayer.cueVideo(youtubeId, 0f)
                }
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
                val message = "Youtube init failure\n $error"
                Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
                    .show()
            }
        })
    }

    private fun initViewListeners() {
        favoriteView?.setOnClickListener {
            viewModel.toggleFavorite()
        }
        wikipediaLinkView?.setOnClickListener {
            context?.openUrl(viewState.data.wikipediaUrl)
        }
    }


    companion object {
        private const val ARG_LAUNCH_ID = "launch_id"

        fun newInstance(launchId: String) =
            LaunchesDetailsFragment().apply {
                arguments = Bundle().also {
                    it.putString(ARG_LAUNCH_ID, launchId)
                }
            }
    }
}
