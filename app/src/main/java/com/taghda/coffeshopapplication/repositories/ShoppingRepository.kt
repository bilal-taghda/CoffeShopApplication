package com.taghda.coffeshopapplication.repositories

import androidx.lifecycle.LiveData
import com.taghda.coffeshopapplication.data.local.ShoppingItem
import com.taghda.coffeshopapplication.data.remote.responses.ImageResponse
import com.taghda.coffeshopapplication.data.remote.responses.ImageResponsee
import com.taghda.coffeshopapplication.other.Resource
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): Flow<List<ShoppingItem>>

    fun observeTotalPrice(): Flow<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponsee>
}