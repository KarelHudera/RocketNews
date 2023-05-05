package com.example.rocketnews.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.imageLoader
import coil.request.ImageRequest
import com.example.rocketnews.R
import com.example.rocketnews.databaseNasa.NasaItem
import com.example.rocketnews.databaseNasa.NasaItemRepository
import com.example.rocketnews.databinding.FragmentDailyBinding
import com.example.rocketnews.databinding.LayoutErrorLoadingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DailyFragment : Fragment() {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!
    private var _mergeBinding: LayoutErrorLoadingBinding? = null
    private val mergeBinding get() = _mergeBinding!!
    private val repository = NasaItemRepository()
    private val viewModel by viewModels<DailyFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyBinding.inflate(inflater)
        _mergeBinding = LayoutErrorLoadingBinding.bind(binding.root)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mergeBinding.refreshButtonErrorLayout.setOnClickListener {
            viewModel.refreshFragment()
        }

        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            viewLifecycleOwner.lifecycleScope.launch {
                val nasaItem = withContext(Dispatchers.IO) { repository.getAll().firstOrNull() }
                mergeBinding.updateVisibility(state, nasaItem)
            }

            when (state) {
                is DailyFragmentScreenState.Error -> {
                    state.throwable.printStackTrace()
                    mergeBinding.errorMessageErrorLayout.text = "Error: ${state.throwable.localizedMessage}"

                    viewLifecycleOwner.lifecycleScope.launch {
                        val nasaItem = getFirstNasaItem()!!
                        with(binding) {
                            title.text = nasaItem.title
                            titleDown.text = getString(R.string.dailySmallHeader)
                            explanation.text = nasaItem.explanation
                            date.text = nasaItem.date
                            today.text = getString(R.string.dailyHeader)
                        }
                        Toast.makeText(context, "You are offline", Toast.LENGTH_SHORT).show()
                    }
                }
                is DailyFragmentScreenState.Loading -> {
                    // Left blank intentionally
                }
                is DailyFragmentScreenState.Success -> {
                    with(binding) {
                        val request = ImageRequest.Builder(requireContext())
                            .data(state.data.url)
                            .target(onSuccess = { result ->
                                // Set the low-resolution image as the image view's drawable
                                imageView.setImageDrawable(result)

                                // Load the high-resolution image
                                val hdRequest = ImageRequest.Builder(requireContext())
                                    .data(state.data.hdurl)
                                    .target(onSuccess = { hdResult ->
                                        // Set the high-resolution image as the image view's drawable
                                        imageView.setImageDrawable(hdResult)
                                    })
                                    .build()
                                requireContext().imageLoader.enqueue(hdRequest)
                            })
                            .build()
                        requireContext().imageLoader.enqueue(request)
                        title.text = state.data.title
                        titleDown.text = getString(R.string.dailySmallHeader)
                        explanation.text = state.data.explanation
                        date.text = state.data.date
                        today.text = getString(R.string.dailyHeader)
                    }
                }
            }
        }
    }

    private suspend fun getFirstNasaItem(): NasaItem? {
        return repository.getAll().firstOrNull()
    }
}

fun LayoutErrorLoadingBinding.updateVisibility(state: DailyFragmentScreenState, nasaItem: NasaItem?) {
    refreshButtonErrorLayout.isVisible = state is DailyFragmentScreenState.Error && nasaItem == null
    errorMessageErrorLayout.isVisible = state is DailyFragmentScreenState.Error && nasaItem == null
    progressBarErrorLayout.isVisible = state is DailyFragmentScreenState.Loading
}