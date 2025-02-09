package com.example.onboardingassignment.data

import java.util.UUID

data class PostModel(
    val title: String,
    val content: String,
    val writer: String = "default user",
    val viewCount: Int = 0,
    val key: String = UUID.randomUUID().toString(),
    val createdDate: Long = System.currentTimeMillis(),
    val updatedDate: Long = System.currentTimeMillis()
)
