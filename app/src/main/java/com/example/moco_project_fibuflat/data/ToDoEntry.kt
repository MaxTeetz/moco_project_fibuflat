package com.example.moco_project_fibuflat.data

import android.graphics.Bitmap

data class ToDoEntry(
    var id: String? = null,
    var name: String? = null,
    var message: String? = null,
    var pictureAdded: String? = null,
    var picture: Bitmap? = null
)
