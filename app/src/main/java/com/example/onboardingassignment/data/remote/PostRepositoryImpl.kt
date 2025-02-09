package com.example.onboardingassignment.data.remote

import android.util.Log
import com.example.onboardingassignment.data.PostModel
import com.example.onboardingassignment.data.Table
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PostRepositoryImpl: PostRepository {

    private val reference = Firebase.firestore.collection(Table.POST.tableName)

    override suspend fun createPost(post: PostModel) {

        reference.document(post.key).set(post).addOnSuccessListener {
            Log.i("PostRepositoryImpl", "success create post key = ${post.key}")
        }.addOnFailureListener {
            Log.i("PostRepositoryImpl", "fail create post key = ${post.key}")
        }
    }

    override suspend fun selectAllPosts(): MutableList<PostModel> {
        return withContext(Dispatchers.IO) {
            val snapshot = reference.get().await()

            val posts = mutableListOf<PostModel>()

            for (document in snapshot.documents) {
                val hashMap = document.data as HashMap<*, *>
                val gson = Gson()
                val toJson = gson.toJson(hashMap)
                val fromJson = gson.fromJson(toJson, PostModel::class.java)
                posts.add(fromJson)
            }
            posts
        }
    }
}