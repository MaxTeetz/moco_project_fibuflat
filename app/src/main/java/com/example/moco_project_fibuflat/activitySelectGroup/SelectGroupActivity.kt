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
import com.example.moco_project_fibuflat.databinding.ActivitySelectGroupBinding
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SelectGroupActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivitySelectGroupBinding
    private lateinit var database: DatabaseReference
    private val viewModel: SelectGroupActivityViewModel by viewModels()
    private val neededData: OftenNeededData by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainScope().launch {
            neededData.setData()
            updateUI()
        }
        viewModel.groupStatus.observe(this){
            changeActivity()
        }
        viewModel.checkGroupStatus(neededData.dataBaseUsers)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun updateUI() {
        binding = ActivitySelectGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_select_group_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Set up the action bar for use with the NavController
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    private fun changeActivity() {

        //intent for changing activity
        val intent =
            Intent(this@SelectGroupActivity, GroupActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }
}