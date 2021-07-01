package com.taghda.coffeshopapplication.data.remote.responses

data class ImageResponsee(
    val results: List<Result>,
    val total: Int,
    val total_pages: Int
)