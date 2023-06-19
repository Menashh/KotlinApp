package com.example.finalprojectkotlin.data.repository

import com.example.finalprojectkotlin.data.model.User
import com.example.finalprojectkotlin.util.Resource

interface AuthRepository {

    suspend fun currentUser() : Resource<User>
    suspend fun login(email:String, password:String) : Resource<User>
    suspend fun createUser(userEmail:String,
                           userLoginPass:String) : Resource<User>
    fun logout()
}