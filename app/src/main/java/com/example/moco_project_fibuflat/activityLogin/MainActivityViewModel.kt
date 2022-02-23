package com.example.moco_project_fibuflat.activityLogin

import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivityViewModel : ViewModel() {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Users")

    fun setUserDB(
        uid: String,
        name: String,
        email: String,
    ) {
        val user = User(uid, name, email)
        database.child(uid).setValue(user)
    }
}