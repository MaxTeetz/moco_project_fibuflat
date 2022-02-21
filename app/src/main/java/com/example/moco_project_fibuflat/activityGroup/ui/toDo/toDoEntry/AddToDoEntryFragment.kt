package com.example.moco_project_fibuflat.activityGroup.ui.toDo.toDoEntry

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.ToDoEntry
import com.example.moco_project_fibuflat.databinding.FragmentAddToDoEntryBinding
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.*

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class AddToDoEntryFragment : Fragment() {

    private lateinit var viewModel: AddToDoEntryViewModel
    private lateinit var neededData: OftenNeededData
    private lateinit var storageReference: StorageReference
    private lateinit var id: String

    private lateinit var imageUri: Uri
    private lateinit var dialog: Dialog

    private var takenImage: Bitmap? = null

    private var _binding: FragmentAddToDoEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[AddToDoEntryViewModel::class.java]
        neededData = ViewModelProvider(requireActivity())[OftenNeededData::class.java]
        _binding = FragmentAddToDoEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id = UUID.randomUUID().toString()

        binding.optionalImage.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = FileProvider.getUriForFile(requireContext(),
                "com.example.moco_project_fibuflat.fileprovider",
                photoFile)

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null)
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            else
                Toast.makeText(requireContext(), "Unable to open camera", Toast.LENGTH_SHORT).show()
        }

        binding.addEntry.setOnClickListener {
            showProgressBar()
            val todoEntry =
                ToDoEntry(id, neededData.user.value!!.username, binding.task.text.toString(), "ToDoEntries/$id")
            neededData.dataBaseGroups.child(neededData.group.value!!.groupId!!).child("todoEntries")
                .child(id)
                .setValue(todoEntry).addOnCompleteListener {
                    if (it.isSuccessful) {
                        uploadPicture()
                    } else {
                        hideProgressBar()
                        Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun uploadPicture() {
        storageReference = FirebaseStorage.getInstance().getReference("ToDoEntries/$id")
        storageReference.putFile(imageUri).addOnSuccessListener {

            hideProgressBar()
            Toast.makeText(requireContext(), "Successfully uploaded", Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {

            hideProgressBar()
            Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()

        }
    }

    private fun showProgressBar() {
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar() {
        dialog.dismiss()
    }

    private fun getPhotoFile(fileName: String): File {
        //Use "getExternalDir" on Context to access package-specific directories
        val storageDirectory = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, "jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //val takenImage = data?.extras?.get("data") as Bitmap
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding.optionalImage.setImageBitmap(takenImage)
            this.takenImage = takenImage

            imageUri = Uri.fromFile(photoFile)
            Log.d("todoEntry", "${Uri.fromFile(photoFile)}")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}