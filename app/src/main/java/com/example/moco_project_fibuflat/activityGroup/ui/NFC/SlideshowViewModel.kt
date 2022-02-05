package com.example.moco_project_fibuflat.activityGroup.ui.NFC

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is NFC Fragment"
    }
    val text: LiveData<String> = _text
}