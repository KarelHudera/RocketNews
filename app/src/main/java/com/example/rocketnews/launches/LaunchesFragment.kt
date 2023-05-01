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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.request.ImageRequest
import com.example.rocketnews.daily.DailyFragmentScreenState
import com.example.rocketnews.daily.DailyFragmentViewModel
import com.example.rocketnews.database.NasaItem
import com.example.rocketnews.database.NasaItemRepository
import com.example.rocketnews.databinding.FragmentDailyBinding
import com.example.rocketnews.databinding.FragmentLaunchesBinding
import com.example.rocketnews.databinding.LayoutErrorLoadingBinding
import com.example.rocketnews.extensions.safeNavigate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LaunchesFragment: Fragment() {
    private var _binding: FragmentLaunchesBinding? = null
    private val binding get() = _binding!!
    private var _mergeBinding: LayoutErrorLoadingBinding? = null
    private val mergeBinding get() = _mergeBinding!!

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

        val launchesAdapter = LaunchesAdapter ()

        binding.recyclerItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = launchesAdapter
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

                }
            }
        }
    }


}

fun LayoutErrorLoadingBinding.updateVisibility(state: LaunchesFragmentScreenState) {
    refreshButtonErrorLayout.isVisible = state is LaunchesFragmentScreenState.Error
    errorMessageErrorLayout.isVisible = state is LaunchesFragmentScreenState.Error
    progressBarErrorLayout.isVisible = state is LaunchesFragmentScreenState.Loading
}