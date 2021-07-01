package com.taghda.coffeshopapplication.repositories

import com.taghda.coffeshopapplication.data.local.ShoppingItem
import com.taghda.coffeshopapplication.data.remote.responses.ImageResponsee
import com.taghda.coffeshopapplication.other.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeShoppingRepositoryAndroidTest : ShoppingRepository {

    private val shoppingItems = mutableListOf<ShoppingItem>()

    private val observableShoppingItems = MutableStateFlow<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableStateFlow<Float>(0F)

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshFlow() {
        observableShoppingItems.value = (shoppingItems)
        observableTotalPrice.value = (getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumByDouble { it.price.toDouble() }.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshFlow()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshFlow()
    }

    override fun observeAllShoppingItems(): Flow<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): Flow<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponsee> {
        return if(shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(ImageResponsee(listOf(), 0, 0))
        }
    }
}











