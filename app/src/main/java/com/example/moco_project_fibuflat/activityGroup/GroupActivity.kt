package com.example.moco_project_fibuflat.activityGroup

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.databinding.ActivityGroupBinding

class
GroupActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarGroup.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.group, menu)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        this.finishAffinity()

        /*
        * FirebaseAuth.getInstance().signOut()
        * startActivity(Intent(this@GroupActivity, MainActivity::class.java
        * finish
        * */
    }
}