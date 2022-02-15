package com.example.moco_project_fibuflat.activityGroup.ui.nfc

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NfcViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is nfc Fragment"
    }
    val text: LiveData<String> = _text

    override fun onCleared() {
        super.onCleared()
        Log.d("nfcFragmentViewModel", "onCleared()")
    }
}