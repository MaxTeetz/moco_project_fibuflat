package com.example.moco_project_fibuflat.ActivityLogin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.moco_project_fibuflat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Set up the action bar for use with the NavController
        NavigationUI.setupActionBarWithNavController(this, navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun registerUserInFirebase(email: String, password: String) {

        Log.d("RegisterUser", auth.currentUser.toString())

        if (auth.currentUser == null) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {

                if (it.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(this, R.string.register_successful, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.register_failed, Toast.LENGTH_SHORT).show()
                }
            }
        }
        Log.d("RegisterUser", auth.currentUser.toString())
    }

}