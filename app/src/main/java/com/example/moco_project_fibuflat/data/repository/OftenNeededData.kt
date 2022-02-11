package com.example.moco_project_fibuflat.data.repository

import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.User

//data like User or Group etc.
class OftenNeededData : ViewModel() {
    private var _user: User? = null

    fun setUser(user: User) {
        this._user = user
    }

    fun getUser(): User?{
        return _user
    }
}