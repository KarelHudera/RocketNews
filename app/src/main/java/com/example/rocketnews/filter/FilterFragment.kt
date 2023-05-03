package com.example.rocketnews.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rocketnews.databinding.FragmentFilterBinding
import com.example.rocketnews.extensions.safeNavigate
import com.example.rocketnews.launches.LaunchesAdapter
import com.example.rocketnews.launches.LaunchesFragmentScreenState
import androidx.core.widget.addTextChangedListener

class FilterFragment: Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FilterFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(layoutInflater)
        return _binding?.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launchesAdapter = LaunchesAdapter()

        binding.recyclerItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = launchesAdapter
        }

        binding.editTextFilter.addTextChangedListener {
            if (it != null) {
                viewModel.onTextChanged(it.toString())
            }
        }

        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LaunchesFragmentScreenState.Error -> Toast.makeText(
                    requireContext(),
                    "Error",
                    Toast.LENGTH_SHORT
                ).show()
                is LaunchesFragmentScreenState.Loading -> {}
                else -> {}
            }
        }

        viewModel.exchangesList.observe(viewLifecycleOwner) {
            launchesAdapter.submitList(it)
        }
    }
}
