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
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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

        viewModel.groupAccess.observe(this) {
            //setUser() ToDo
            changeActivity(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean { //stays
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun firebaseRegister(
        email: String,
        password: String,
        name: String
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> //ToDo into viewModel and with coroutine
                if (task.isSuccessful) {
                    viewModel.setUserDB(
                        task.result!!.user!!.uid,
                        name,
                        email
                    )
                    changeActivity(GroupAccess.NOGROUP)
                } else
                    taskFailed(task)
            }
    }

    fun firebaseLogin(
        email: String,
        password: String
    ) { //ToDo into viewModel? Performance influence
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    viewModel.getDBGroupEntry()
                else
                    taskFailed(task)
            }
    }

    private fun taskFailed(task: Task<AuthResult>) { //stays
        Toast.makeText(
            this@MainActivity,
            task.exception!!.message.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun changeActivity(groupAccess: GroupAccess) {
        val intent: Intent = if (groupAccess == GroupAccess.INGROUP)
            Intent(this@MainActivity, GroupActivity::class.java)
        else
            Intent(this@MainActivity, SelectGroupActivity::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

}