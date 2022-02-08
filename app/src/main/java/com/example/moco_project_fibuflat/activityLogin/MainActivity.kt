package com.example.moco_project_fibuflat.activityLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activitySelectGroup.SelectGroupActivity
import com.example.moco_project_fibuflat.data.User
import com.example.moco_project_fibuflat.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

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

    fun firebaseRegister(email: String, password: String, name: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setUserDB(
                        task.result!!.user!!.uid,
                        name,
                        email
                    )
                    taskSuccessful(
                        "Register Successful",
                        email,
                        task.result!!.user!!,
                    )
                } else
                    taskFailed(task)
            }
    }

    fun firebaseLogin(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    taskSuccessful(
                        "Login Successful",
                        email,
                        FirebaseAuth.getInstance().currentUser!!,
                    )
                else
                    taskFailed(task)
            }
    }

    private fun taskSuccessful(
        text: String,
        email: String,
        firebaseUser: FirebaseUser
    ) {
        Toast.makeText(
            this@MainActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()


        val intent =
            Intent(this@MainActivity, SelectGroupActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        intent.putExtra(
            "user_id", firebaseUser.uid
        )

        intent.putExtra(
            "email_id", email
        )

        startActivity(intent)

    }

    private fun taskFailed(task: Task<AuthResult>) {
        Toast.makeText(
            this@MainActivity,
            task.exception!!.message.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setUserDB(uid: String, name: String, email: String) {
        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
        val user = User(uid, name, email, UUID.randomUUID())

        database.child(uid).setValue(user)
            .addOnSuccessListener {
                Log.d("mainActivity", "createdDBInstance")
            }
            .addOnFailureListener {
                Log.d("mainActivity", "couldn't create db entry")
            }
    }

    private fun getDBGroupEntry(): Boolean{
        return true
    }
}