package com.example.finalprojectkotlin.ui

class User(email: String?, uid: String?) {
    private var email: String? = email
    var uid: String? = uid

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun fetchUid(): String? {
        return uid
    }

    fun assignUid(uid: String?) {
        this.uid = uid
    }
}
