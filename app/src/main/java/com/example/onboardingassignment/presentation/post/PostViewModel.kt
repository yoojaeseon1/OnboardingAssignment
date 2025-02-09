package com.example.onboardingassignment.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onboardingassignment.data.PostModel
import com.example.onboardingassignment.data.remote.PostRepository
import com.example.onboardingassignment.data.remote.PostRepositoryImpl
import kotlinx.coroutines.launch

class PostViewModel: ViewModel() {

    private val _posts = MutableLiveData<MutableList<PostModel>>()
    val posts get() = _posts

    private val postRepository: PostRepository by lazy {
        PostRepositoryImpl()
    }

    suspend fun createPost(post: PostModel) {
        viewModelScope.launch {
            postRepository.createPost(post)
        }
    }

    suspend fun selectAllPostList(){
        viewModelScope.launch {
            val selectAllPosts = postRepository.selectAllPosts()
            posts.value = selectAllPosts
        }
    }


}