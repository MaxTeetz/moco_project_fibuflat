package com.example.moco_project_fibuflat.data.repository

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private var _group: Group? = null

    fun getUser(): User? {
        return _user
    }

    fun getGroup(): Group? {
        return _group
    }

    suspend fun setData() {
        viewModelScope.launch(Dispatchers.Default) {
            val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
            Log.d("dataUser0", "user.toString()")
            fetchUser(uid)
            fetchGroup(uid)
        }
    }

    private suspend fun fetchUser(uid: String): User =
        withContext(Dispatchers.Default) { //ToDo I.O.?
            Log.d("dataUser1", "user.toString()")
            getUserData(uid)
            return@withContext User() //ToDo
        }

    private fun getUserData(uid: String){
        var user: User? = null
        _dataBaseUsers.child(uid).get()
            .addOnSuccessListener {
                user = it.getValue(User::class.java)!!
                Log.d("dataUser2", user.toString())
                _user = user
            }
    }

    private suspend fun fetchGroup(uid: String): Group =
        withContext(Dispatchers.IO) {
            Log.d("dataUser3", "user.toString()")

            getGroupData(uid)
            return@withContext Group()
        }

    private fun getGroupData(uid: String) {
        var group: Group? = null
        Log.d("dataUser4", "$uid")

        _dataBaseUsers.child(uid).child("group").get().addOnSuccessListener {
            Log.d("dataUser5", it.toString())
            group = it.getValue(Group::class.java)!!
            _user = User(
                _user?.userID,
                _user?.username,
                _user?.email,
                group?.groupId,
                group?.groupName)
            _group = group
            Log.d("dataUser6", _user.toString())
        }
    }
}
