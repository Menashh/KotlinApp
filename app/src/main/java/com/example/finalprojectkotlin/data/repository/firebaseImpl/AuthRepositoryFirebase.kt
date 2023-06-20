package com.example.finalprojectkotlin.data.repository.firebaseImpl

import com.example.finalprojectkotlin.data.model.User
import com.example.finalprojectkotlin.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


import com.example.finalprojectkotlin.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.example.finalprojectkotlin.util.safeCall

class AuthRepositoryFirebase : AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userRef = FirebaseFirestore.getInstance().collection("users")

    override suspend fun currentUser(): Resource<User> {
       return withContext(Dispatchers.IO) {
           safeCall {
               val user = userRef.document(firebaseAuth.currentUser!!.uid).get().await().toObject(User::class.java)
               Resource.Success(user!!)
           }
       }
    }

    override suspend fun login(email: String, password: String): Resource<User> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val result  = firebaseAuth.signInWithEmailAndPassword(email,password).await()
                val user = userRef.document(result.user?.uid!!).get().await().toObject(User::class.java)!!
                Resource.Success(user)
            }
        }
    }

    override suspend fun createUser(
        userEmail: String,
        userLoginPass: String
    ): Resource<User> {
        return withContext(Dispatchers.IO) {
            safeCall {
                val registrationResult  = firebaseAuth.createUserWithEmailAndPassword(userEmail,userLoginPass).await()
                val userId = registrationResult.user?.uid!!
                val newUser = User(userEmail,userLoginPass)
                userRef.document(userId).set(newUser).await()
                Resource.Success(newUser)
            }

        }


    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}