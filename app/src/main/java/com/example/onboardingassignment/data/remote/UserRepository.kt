package com.example.onboardingassignment.data.remote

import com.example.onboardingassignment.data.UserModel

interface UserRepository {

    suspend fun createUser(user: UserModel)
    suspend fun selectUser(id: String): UserModel
}