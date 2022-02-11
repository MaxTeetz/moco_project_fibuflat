package com.example.moco_project_fibuflat.activityLogin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.GroupAccess
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivityViewModel : ViewModel() {
    private lateinit var database: DatabaseReference
    private val _groupAccess = MutableLiveData<GroupAccess>()
    val groupAccess: LiveData<GroupAccess> get() = _groupAccess

    fun setUserDB(
        uid: String,
        name: String,
        email: String
    ) { //ToDo return if db entry was created
        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(uid)
        val user = User(uid, name, email)
        database.setValue(user)
    }

    fun getDBGroupEntry() {

        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(FirebaseAuth.getInstance().uid.toString())
                .child("group")
        database.get().addOnSuccessListener {
            if (it.value == null)
                _groupAccess.value = GroupAccess.NOGROUP
            else
                _groupAccess.value = GroupAccess.INGROUP


        }.addOnFailureListener {
            Log.d("mainActivity", "no Data retrieved")
        }
    }

}