package com.example.moco_project_fibuflat.activityGroup.ui.ToDo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is ToDo Fragment"
    }
    val text: LiveData<String> = _text
}