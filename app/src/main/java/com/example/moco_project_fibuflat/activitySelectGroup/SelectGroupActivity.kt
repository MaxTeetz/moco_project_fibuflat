package com.example.moco_project_fibuflat.activitySelectGroup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.data.Group
import com.example.moco_project_fibuflat.databinding.ActivitySelectGroupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SelectGroupActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivitySelectGroupBinding
    private lateinit var database: DatabaseReference
    private val viewModel: SelectGroupActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_select_group_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Set up the action bar for use with the NavController
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun fireBaseCreateGroup(name: String) { //Todo make clean and better
        val groupID = UUID.randomUUID().toString()
        val group = Group(groupID, name)
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        //get user -> groupNode
        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users").child(userID)
                .child("group")

        //set group -> name and id
        database.setValue(group).addOnSuccessListener {

            //intent for changing activity
            val intent =
                Intent(this@SelectGroupActivity, GroupActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            viewModel.createGroup(group, userID)
            //extras
            startActivity(intent)
        }
    }
}