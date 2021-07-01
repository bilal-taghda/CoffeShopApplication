package com.taghda.coffeshopapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.taghda.coffeshopapplication.R
import com.taghda.coffeshopapplication.other.Status
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import com.taghda.coffeshopapplication.other.Status.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingItemFragment @Inject constructor(
    val glide: RequestManager
) : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var viewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)
        subscribeToObservers()

        btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                etShoppingItemName.text.toString(),
                etShoppingItemAmount.text.toString(),
                etShoppingItemPrice.text.toString()
            )
        }

        ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCurImageUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.curImageUrl.collect {
                glide.load(it).into(ivShoppingImage)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.insertShoppingItemStatus.collect {
                it.getContentIfNotHandled()?.let { result ->
                   when (result.status) {
                        SUCCESS -> {
                            Snackbar.make(
                                requireActivity().rootLayout,
                                "Added Shopping Item",
                                Snackbar.LENGTH_LONG
                            ).show()
                            findNavController().popBackStack()
                        }
                        ERROR -> {
                            Snackbar.make(
                                requireActivity().rootLayout,
                                result.message ?: "An unknown error occcured",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                   }
                }
            }
        }
    }
}













