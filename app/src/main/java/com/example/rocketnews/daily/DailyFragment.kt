package com.example.rocketnews.daily

import android.annotation.SuppressLint
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
import com.example.rocketnews.database.NasaItem
import com.example.rocketnews.database.NasaItemRepository
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
    ): View? {
        _binding = FragmentDailyBinding.inflate(layoutInflater)
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
                            titleDown.text = "Explanation"
                            explanation.text = nasaItem.explanation
                            date.text = nasaItem.date
                            today.text = "Today"
                        }
                        Toast.makeText(context, "You are offline", Toast.LENGTH_SHORT).show()
                    }

                }
                is DailyFragmentScreenState.Loading -> {
                    //Left blank intentionally
                }
                is DailyFragmentScreenState.Success -> {
                    with(binding) {
                        val let = context?.let { ctx ->
                            val request = ImageRequest.Builder(ctx)
                                .data(state.data.url)
                                .target(onSuccess = { result ->
                                    imageView.setImageDrawable(result)
                                    imageView.postDelayed({
                                        val hdRequest = ImageRequest.Builder(ctx)
                                            .data(state.data.hdurl)
                                            .target(onSuccess = { hdResult ->
                                                imageView.setImageDrawable(hdResult)
                                            })
                                            .build()
                                        ctx.imageLoader.enqueue(hdRequest)
                                    }, 1000)
                                })
                                .build()
                            ctx.imageLoader.enqueue(request)
                        }
                        title.text = state.data.title
                        titleDown.text = "Explanation"
                        explanation.text = state.data.explanation
                        date.text = state.data.date
                        today.text = "Today"
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