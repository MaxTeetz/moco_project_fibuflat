package com.example.moco_project_fibuflat.data.repository

import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

//data like User or Group etc.
class OftenNeededData : ViewModel() {

    //shortcuts
    private val _dataBaseUsers: DatabaseReference =
        FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")
    val dataBaseUsers get() = _dataBaseUsers

    private val _dataBaseGroups: DatabaseReference =
        FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Groups")
    val dataBaseGroups get() = _dataBaseGroups

    private var _user: User? = null

    fun setUser(user: User) {
        this._user = user
    }

    fun getUser(): User? {
        return _user
    }
}