package com.example.moco_project_fibuflat.activityGroup

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activitySelectGroup.SelectGroupActivity
import com.example.moco_project_fibuflat.data.Connectivity
import com.example.moco_project_fibuflat.databinding.ActivityGroupBinding
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class
GroupActivity : AppCompatActivity() {

    private lateinit var dialog: Dialog
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGroupBinding
    private val viewModel: OftenNeededData by viewModels()
    private var connectivity: Connectivity? = null
    private val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(connectivityReceiver, intentFilter)

        MainScope().launch {
            Log.d("GroupActivityCoroutine", "0")
            showProgressBar()
            viewModel.setData()
            Log.d("GroupActivityCoroutine", "6")
            hideProgressBar()
            updateUI()
        }
    }

    private fun showProgressBar() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar() {
        dialog.dismiss()
    }

    private suspend fun updateUI() {

        withContext(Dispatchers.Main) {
            Log.d("GroupActivityCoroutine", "7")
            binding = ActivityGroupBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setSupportActionBar(binding.appBarGroup.toolbar)
            binding.appBarGroup

            val drawerLayout: DrawerLayout = binding.drawerLayout
            val navView: NavigationView = binding.navView
            val navController = findNavController(R.id.nav_host_fragment_content_group)

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_home,
                    R.id.nav_todo_list,
                    R.id.nav_nfc,
                    R.id.nav_group_management,
                    R.id.nav_group_info
                ), drawerLayout
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_group)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.group, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        this.finishAffinity()
    }

    suspend fun changeToSelectGroup() {
        withContext(Dispatchers.Main) {
            val intent = Intent(this@GroupActivity, SelectGroupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    //used for restarting activity if connection was gone
    private val connectivityReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
                val noConnectivity: Boolean =
                    intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                if (noConnectivity) {
                    Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
                    connectivity = Connectivity.OFFLINE
                    viewModel.setConnectivity(Connectivity.OFFLINE)
                } else {
                    if (connectivity == Connectivity.OFFLINE) {
                        Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
                        val intentRestart = Intent(this@GroupActivity, GroupActivity::class.java)
                        startActivity(intentRestart)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {}
}