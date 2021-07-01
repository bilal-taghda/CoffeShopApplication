package com.taghda.coffeshopapplication.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.taghda.coffeshopapplication.R
import com.taghda.coffeshopapplication.adapters.ImageAdapter
import com.taghda.coffeshopapplication.other.Constants.GRID_SPAN_COUNT
import com.taghda.coffeshopapplication.other.Constants.SEARCH_TIME_DELAY
import com.taghda.coffeshopapplication.other.Status
import com.google.android.material.snackbar.Snackbar
import com.taghda.coffeshopapplication.other.Event
import com.taghda.coffeshopapplication.other.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_image_pick.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter
) : Fragment(R.layout.fragment_image_pick) {

    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        setupRecyclerView()
        subscribeToObservers()

        var job: Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(SEARCH_TIME_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchForImage(editable.toString())
                    }
                }
            }
        }

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setCurImageUrl(it)
            imageAdapter.images = emptyList()
        }
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.images.collect {
                it.getContentIfNotHandled()?.let { result ->
                    when (result.status) {
                        Status.SUCCESS -> {
                            val urls =
                                result.data?.results?.map { imageResult -> imageResult.urls.small }
                            imageAdapter.images = urls ?: listOf()
                            progressBar.visibility = View.GONE
                        }
                        Status.ERROR -> {
                            Snackbar.make(
                                requireActivity().rootLayout,
                                result.message ?: "An unknown error occured.",
                                Snackbar.LENGTH_LONG
                            ).show()
                            progressBar.visibility = View.GONE
                        }
                        Status.LOADING -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        Status.EMPTY -> {
                            progressBar.visibility = View.GONE
                        }

                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }
}