package com.example.onboardingassignment.data.remote

import com.example.onboardingassignment.data.PostModel

interface PostRepository {

    suspend fun createPost(post: PostModel)
    suspend fun selectAllPosts(): MutableList<PostModel>
}