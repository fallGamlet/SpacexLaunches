package com.spacex.launches.ui.launches.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.spacex.launches.R
import com.spacex.launches.getNavigationProvider
import com.spacex.launches.getRepositoryProvider
import com.spacex.launches.ui.adapters.SimpleRecyclerAdapter
import java.text.SimpleDateFormat
import java.util.*

class LaunchesPreviewFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var emptyView: View? = null
    private var errorView: TextView? = null
    private var loadingView: View? = null
    private var viewState: LaunchPreviewsViewState = LaunchPreviewsViewState.Loading()
    private val dateFormatter =
        SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault())

    private val viewModel: LaunchesPreviewViewModel by viewModels {
        val repository = getRepositoryProvider()
            .getLaunchRepository()
        LaunchesPreviewViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            viewModel.viewState.collect {
                updateState(it)
            }
        }
    }

    private fun updateState(state: LaunchPreviewsViewState) {
        //TODO make to use DiffUtils
        if (viewState.items != state.items) {
            recyclerView?.adapter?.notifyDataSetChanged()
        }

        viewState = state

        when (state) {
            is LaunchPreviewsViewState.Loading -> {
                loadingView?.isVisible = true
                emptyView?.isVisible = false
                errorView?.isVisible = false
            }
            is LaunchPreviewsViewState.Success -> {
                loadingView?.isVisible = false
                emptyView?.isVisible = false
                errorView?.isVisible = false
            }
            is LaunchPreviewsViewState.Error -> {
                val hasData = state.items.isNotEmpty()
                loadingView?.isVisible = false
                emptyView?.isVisible = false
                errorView?.isVisible = !hasData
                if (hasData) {
                    Snackbar.make(
                        requireView(),
                        state.exception.message ?: "Error",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
        emptyView = null
        errorView = null
        loadingView = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.screen_launch_preview_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initRecyclerView()

        viewModel.loadData()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        errorView = view.findViewById(R.id.errorView)
        emptyView = view.findViewById(R.id.emptyView)
        loadingView = view.findViewById(R.id.loadingView)
    }

    private fun initRecyclerView() {
        recyclerView?.adapter = SimpleRecyclerAdapter(
            provideCount = { viewState.items.size },
            provideHolder = { parent, _ ->
                val view = layoutInflater.inflate(R.layout.screen_launch_preview_list_item, parent, false)
                LaunchPreviewViewHolder(
                    view = view,
                    provideData = ::getLaunchPreview,
                    formatDate = ::formatData,
                    onClickListener = ::openLaunchDetails,
                    onFavoriteToggleClickListener = ::onFavoriteToggle,
                )
            }
        )
    }

    private fun getLaunchPreview(position: Int) =
        viewState.items[position]

    private fun formatData(date: Date): String {
        if (date.time == 0L) return ""

        return dateFormatter.format(date)
    }

    private fun openLaunchDetails(position: Int) {
        val item = viewState.items.getOrNull(position) ?: return
        getNavigationProvider().openLaunchDetails(item.id)
    }

    private fun onFavoriteToggle(position: Int) {
        val item = viewState.items.getOrNull(position) ?: return
        viewModel.toggleFavorite(item.id)
    }


    companion object {
        fun newInstance() = LaunchesPreviewFragment()
    }
}
