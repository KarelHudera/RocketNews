package com.example.rocketnews.launches

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rocketnews.databaseSpaceX.SpaceXItem
import com.example.rocketnews.databaseSpaceX.SpaceXItemDao
import com.example.rocketnews.databaseSpaceX.SpaceXItemRepository
import com.example.rocketnews.databinding.FragmentLaunchesBinding
import com.example.rocketnews.databinding.LayoutErrorLoadingBinding
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LaunchesFragment: Fragment() {
    private var _binding: FragmentLaunchesBinding? = null
    private val binding get() = _binding!!
    private var _mergeBinding: LayoutErrorLoadingBinding? = null
    private val mergeBinding get() = _mergeBinding!!

    private val launchesAdapter = LaunchesAdapter ()
    private val launchesFavouriteAdapterAdapter = LaunchesFavouriteAdapter()
    private val spaceXItemRepository = SpaceXItemRepository()

    private val viewModel by viewModels<LaunchesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLaunchesBinding.inflate(layoutInflater)
        _mergeBinding = LayoutErrorLoadingBinding.bind(binding.root)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mergeBinding.refreshButtonErrorLayout.setOnClickListener {
            viewModel.refreshFragment()
        }

        binding.recyclerItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = launchesAdapter
        }

        binding.recyclerFavouriteItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = launchesFavouriteAdapterAdapter
        }


        viewModel.screenState.observe(viewLifecycleOwner) { state ->

            viewLifecycleOwner.lifecycleScope.launch {
                mergeBinding.updateVisibility(state)
            }

            when (state) {
                is LaunchesFragmentScreenState.Error -> {
                    state.throwable.printStackTrace()
                    mergeBinding.errorMessageErrorLayout.text = "Error: ${state.throwable.localizedMessage}"
                }
                is LaunchesFragmentScreenState.Loading -> {
                    //Left blank intentionally
                }
                is LaunchesFragmentScreenState.Success -> {
                    launchesAdapter.submitList(state.data)
                    viewLifecycleOwner.lifecycleScope.launch {
                        launchesFavouriteAdapterAdapter.submitList(getSpaceXItems())
                    }
                    binding.favourites.text = "Favourites"
                }
            }
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

