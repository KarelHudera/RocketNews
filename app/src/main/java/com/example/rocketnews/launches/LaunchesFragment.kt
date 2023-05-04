package com.example.rocketnews.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rocketnews.databaseSpaceX.SpaceXItem
import com.example.rocketnews.databaseSpaceX.SpaceXItemRepository
import com.example.rocketnews.databinding.FragmentLaunchesBinding
import com.example.rocketnews.databinding.LayoutErrorLoadingBinding
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class LaunchesFragment : Fragment() {

    private var _binding: FragmentLaunchesBinding? = null
    private val binding get() = _binding!!

    private var _mergeBinding: LayoutErrorLoadingBinding? = null
    private val mergeBinding get() = _mergeBinding!!

    private val launchesAdapter = LaunchesAdapter()
    private val launchesFavouriteAdapter = LaunchesFavouriteAdapter()
    private val spaceXItemRepository = SpaceXItemRepository()

    private val viewModel by viewModels<LaunchesFragmentViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        _mergeBinding = LayoutErrorLoadingBinding.bind(binding.root)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        with(binding) {
            recyclerItems.layoutManager = LinearLayoutManager(context)
            recyclerItems.adapter = launchesAdapter

            recyclerFavouriteItems.layoutManager = LinearLayoutManager(context)
            recyclerFavouriteItems.adapter = launchesFavouriteAdapter

            recyclerItemsFilter.layoutManager = LinearLayoutManager(context)
            recyclerItemsFilter.adapter = launchesAdapter

            mergeBinding.refreshButtonErrorLayout.setOnClickListener {
                viewModel.refreshFragment()
            }

            unPinAll.apply {
                text = "Unpin all"
                setOnClickListener {
                    viewModel.viewModelScope.launch {
                        spaceXItemRepository.deleteAllSpaceX()
                    }
                }
            }

            editTextFilter.addTextChangedListener {
                viewModel.onTextChanged(it.toString())
                updateFilterViewsVisibility(it?.isNotEmpty() == true)
            }

            pinned.text = "Pinned"
            upcoming.text = "Upcoming"
            sortBy.text = "Sort by"
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            mergeBinding.updateVisibility(state)

            when (state) {
                is LaunchesFragmentScreenState.Error -> {
                    state.throwable.printStackTrace()
                    mergeBinding.errorMessageErrorLayout.text = "Error: ${state.throwable.localizedMessage}"
                }
                is LaunchesFragmentScreenState.Loading -> {
                    // Left blank intentionally
                }
                is LaunchesFragmentScreenState.Success -> {
                    binding.recyclerItemsFilter.isVisible = false
                    launchesAdapter.submitList(state.data)

                    viewLifecycleOwner.lifecycleScope.launch {
                        launchesFavouriteAdapter.submitList(getSpaceXItems())
                    }
                }
            }
        }

        viewModel.spacexItemList.observe(viewLifecycleOwner) {
            launchesAdapter.submitList(it)
        }
    }

    private fun updateFilterViewsVisibility(hasText: Boolean) {
        with(binding) {
            pinned.isVisible = !hasText
            unPinAll.isVisible = !hasText
            upcoming.isVisible = !hasText
            sortBy.isVisible = !hasText
            recyclerItems.isVisible = !hasText
            recyclerFavouriteItems.isVisible = !hasText
            recyclerItemsFilter.isVisible = hasText
        }
    }

    private suspend fun getSpaceXItems(): List<SpaceXItem> = coroutineScope {
        spaceXItemRepository.getAllSpaceX()
    }
}

fun LayoutErrorLoadingBinding.updateVisibility(state: LaunchesFragmentScreenState) {
    refreshButtonErrorLayout.isVisible = state is LaunchesFragmentScreenState.Error
    errorMessageErrorLayout.isVisible = state is LaunchesFragmentScreenState.Error
    progressBarErrorLayout.isVisible = state is LaunchesFragmentScreenState.Loading
}