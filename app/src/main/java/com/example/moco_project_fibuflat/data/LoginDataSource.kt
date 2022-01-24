package com.example.moco_project_fibuflat.data

import android.util.Log
import com.example.moco_project_fibuflat.data.model.LoggedInUser

class LoginDataSource {
    private var _loggedInUser: LoggedInUser? = null
    private val loggedInUser get() = _loggedInUser!!

    fun setUser(email: String, username: String) {
        _loggedInUser = LoggedInUser(email, username)
    }

    fun getUser(): LoggedInUser {
        Log.d("setUser", loggedInUser.username)
        return loggedInUser
    }
}