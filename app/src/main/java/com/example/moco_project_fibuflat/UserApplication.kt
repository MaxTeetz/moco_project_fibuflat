package com.example.moco_project_fibuflat

import android.app.Application
import com.example.moco_project_fibuflat.data.database.DatabaseUserDatabase

class UserApplication : Application(){
    val database: DatabaseUserDatabase by lazy { DatabaseUserDatabase.getDatabase(this) }
}