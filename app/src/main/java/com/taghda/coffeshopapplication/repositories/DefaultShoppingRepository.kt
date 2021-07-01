package com.taghda.coffeshopapplication.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.taghda.coffeshopapplication.data.local.ShoppingDao
import com.taghda.coffeshopapplication.data.local.ShoppingItem
import com.taghda.coffeshopapplication.data.remote.PixabayAPI
import com.taghda.coffeshopapplication.data.remote.responses.ImageResponse
import com.taghda.coffeshopapplication.data.remote.responses.ImageResponsee
import com.taghda.coffeshopapplication.other.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): Flow<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): Flow<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponsee> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            if(response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Log.e("tag", "searchForImage error" + response.code() )
                Resource.error("An unknown error occured", null)
            }
        } catch(e: Exception) {
            Log.e("EXCEPTION", "EXCEPTION:", e)
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}














