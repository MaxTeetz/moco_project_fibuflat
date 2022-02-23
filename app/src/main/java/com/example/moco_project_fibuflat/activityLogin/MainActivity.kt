package com.example.moco_project_fibuflat.activityLogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.activitySelectGroup.SelectGroupActivity
import com.example.moco_project_fibuflat.data.GroupAccess
import com.example.moco_project_fibuflat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

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

    override fun onSupportNavigateUp(): Boolean { //stays
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun firebaseRegister(
        email: String,
        password: String,
        name: String
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,
            password) //ToDo if it fails. But cleaner this way
            .addOnSuccessListener { task ->
                viewModel.setUserDB(
                    task!!.user!!.uid,
                    name,
                    email
                )
                changeActivity(GroupAccess.NOGROUP)
            }.addOnFailureListener { task ->
                taskFailed(task)
            }
    }

fun taskFailed(task: Exception) { //stays
    Toast.makeText(
        this@MainActivity,
        task.message.toString(),
        Toast.LENGTH_SHORT
    ).show()
}

fun changeActivity(groupAccess: GroupAccess) {
    val intent: Intent = if (groupAccess == GroupAccess.INGROUP)
        Intent(this@MainActivity, GroupActivity::class.java)
    else
        Intent(this@MainActivity, SelectGroupActivity::class.java)

    intent.flags =
        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    intent.putExtra("male", "male")
    startActivity(intent)
}
}