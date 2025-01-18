package com.example.onboardingassignment

interface UserRepository {

    suspend fun createUser(user: UserModel)
    suspend fun selectUser(id: String): UserModel
}