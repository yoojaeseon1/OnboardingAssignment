package com.example.onboardingassignment.data.remote

import android.util.Log
import com.example.onboardingassignment.data.Table
import com.example.onboardingassignment.data.UserModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepositoryImpl: UserRepository {

    private val reference = Firebase.firestore.collection(Table.USERS.tableName)

    override suspend fun createUser(user: UserModel) {
        reference.document(user.id).set(user).addOnSuccessListener {
            Log.i("UserRepositoryImpl", "success sign up = ${it}")
        }.addOnFailureListener {
            Log.i("UserRepositoryImpl", "fail sign up = ${it}")
        }
    }

    override suspend fun selectUser(id: String): UserModel {
        return withContext(Dispatchers.IO) {
            val snapshot = reference.whereEqualTo("id", id).get().await()
            var user = UserModel()
            if(snapshot.documents.size == 0 || snapshot.documents.size > 1)
                return@withContext user

            for (document in snapshot.documents) {
                val hashMap = document.data as HashMap<*,*>
                val gson = Gson()
                val toJson = gson.toJson(hashMap)
                val selectedUser = gson.fromJson(toJson, UserModel::class.java)
                user = selectedUser
            }
            user
        }
    }
}