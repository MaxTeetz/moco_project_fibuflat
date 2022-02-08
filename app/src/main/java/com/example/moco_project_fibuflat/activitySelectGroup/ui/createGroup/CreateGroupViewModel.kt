package com.example.moco_project_fibuflat.activitySelectGroup.ui.createGroup

import androidx.lifecycle.ViewModel

class CreateGroupViewModel: ViewModel() {

    fun checkIfFilled(name: String): Boolean{
        return name.isBlank()
    }
}