package com.example.moco_project_fibuflat.activitySelectGroup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.databinding.ActivitySelectGroupBinding
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SelectGroupActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivitySelectGroupBinding
    private val viewModel: SelectGroupActivityViewModel by viewModels()
    private val neededData: OftenNeededData by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainScope().launch {
            neededData.setData()
            updateUI()
        }

        viewModel.groupStatus.observe(this) {
            Log.d("SelectGroupActivity", "$it")
            changeActivity()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private suspend fun updateUI() {
        binding = ActivitySelectGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Retrieve NavController from the NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_select_group_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Set up the action bar for use with the NavController
        NavigationUI.setupActionBarWithNavController(this, navController)

        lifecycleScope.launch(Dispatchers.IO) { //ToDoEntry witchContext(Dispatchers.IO)?
            viewModel.checkGroupStatus(neededData.dataBaseUsers, neededData.user.value!!)
        }
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