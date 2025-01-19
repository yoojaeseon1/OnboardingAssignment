package com.example.onboardingassignment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class SignViewModel: ViewModel() {

    private var _signInUser = MutableLiveData<UserModel>()
    val signInUser get() = _signInUser

    private var _signInUserByGoogle = MutableLiveData<UserModel>()
    val signInUserByGoogle get() = _signInUserByGoogle

    private val userRepository: UserRepositoryImpl by lazy{
        UserRepositoryImpl()
    }

    suspend fun createUser(user: UserModel) {

        viewModelScope.launch {
            userRepository.createUser(user)
        }
    }

    suspend fun selectUser(id: String){
        viewModelScope.launch{
            signInUser.value = userRepository.selectUser(id)
        }
    }

    suspend fun selectUserByGoogle(id: String){
        viewModelScope.launch{
            signInUserByGoogle.value = userRepository.selectUser(id)
        }
    }


}