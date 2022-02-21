package com.example.moco_project_fibuflat.helperClasses

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class GetStoragePicture(
    uri: String,
    private val giveBitmap: (bitmap: Bitmap?) -> Unit,
){
    private val storageReference = FirebaseStorage.getInstance().reference.child(uri)
    private val localFile = File.createTempFile("tempImage", "jpg")
    init {
        storageReference.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            giveBitmap(bitmap)
        }.addOnFailureListener {
            giveBitmap(null)
        }
    }
}