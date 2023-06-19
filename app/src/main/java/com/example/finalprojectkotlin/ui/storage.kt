package com.example.finalprojectkotlin.ui

class storage {
    private val uid: String? = null

    fun setCurrentUID(uid: String?) {
        if (uid != null) {
            storage.uid = uid
        }
    }

    fun getCurrentUID(): String? {
        return storage.uid
    }

    companion object {
        fun setCurrentUID(currentUID: String?) {
            storage.uid = uid
        }

        lateinit var uid: String
    }
}