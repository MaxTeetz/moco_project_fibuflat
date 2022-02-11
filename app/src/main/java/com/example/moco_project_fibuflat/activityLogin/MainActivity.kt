package com.example.moco_project_fibuflat.activityLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.activitySelectGroup.SelectGroupActivity
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.data.GroupAccess
import com.example.moco_project_fibuflat.data.User
import com.example.moco_project_fibuflat.data.repository.OftenNeededData
import com.example.moco_project_fibuflat.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private val neededDataViewModel: OftenNeededData by viewModels()

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
            setUser()
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
    ) { //ToDo into viewModel? Performance influence
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> //ToDo work with thread -> lazy?
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

    private fun changeActivity(groupAccess: GroupAccess) { //ToDo into viewModel? Performance influence
        val intent: Intent = if (groupAccess == GroupAccess.INGROUP)
            Intent(this@MainActivity, GroupActivity::class.java)
        else
            Intent(this@MainActivity, SelectGroupActivity::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    private fun setUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()

        val database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(uid)

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user: User
                val group: Group
                if (snapshot.exists()) {
                    user = snapshot.getValue(User::class.java)!!

                    if (snapshot.child("group").exists()) {
                        group = snapshot.child("group").getValue(Group::class.java)!!
                        neededDataViewModel.setUser(
                            User(
                                user.userID,
                                user.username,
                                user.email,
                                group.groupId,
                                group.groupName
                            )
                        )
                    } else
                        neededDataViewModel.setUser(User(user.userID, user.username, user.email))
                }
                Log.d("mainActivity", neededDataViewModel.getUser().toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("mainActivity", "cancelled")
            }
        }
        database.addListenerForSingleValueEvent(valueEventListener)
    }

}