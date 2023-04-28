package com.example.rocketnews.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.imageLoader
import coil.request.ImageRequest
import com.example.rocketnews.databinding.FragmentDailyBinding

class DailyFragment: Fragment() {
    private var _binding: FragmentDailyBinding? = null
    private val binding get() = _binding!!


    private val viewModel by viewModels<DailyFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DailyFragmentScreenState.Error -> {
                    //
                }
                is DailyFragmentScreenState.Loading -> {
                    //Left blank intentionally
                }
                is DailyFragmentScreenState.Success -> {


                    with(binding) {
                        context?.let { ctx ->
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
}