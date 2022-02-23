package com.example.moco_project_fibuflat.activityGroup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.ToDoEntry
import com.example.moco_project_fibuflat.databinding.DetailTodoEntryBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class ToDoAdapter(
    private val itemClickListenerButton: ClickListenerButton
) :
    ListAdapter<ToDoEntry, ToDoAdapter.MyViewHolder>(DiffCallback) {

    class MyViewHolder(
        val binding: DetailTodoEntryBinding,
        private var parent: ViewGroup,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val storageReference: StorageReference = FirebaseStorage.getInstance().reference
        private val file: File = File.createTempFile("tempImage", "jpg")

        fun bind(toDoEntry: ToDoEntry) {
            binding.apply {
                todoEntryTextName.text = toDoEntry.name
                todoEntryTextTask.text = toDoEntry.message
                if (toDoEntry.pictureAdded != null) {
                    if (toDoEntry.picture == null)
                        todoEntryPicture.setImageResource(R.drawable.loading_image)
                    else {
                        CoroutineScope(Dispatchers.Main).launch {
                            Glide.with(itemView).load(toDoEntry.picture)
                                .into(binding.todoEntryPicture)
                        }
                    }

                } else
                    todoEntryPicture.setImageResource(R.drawable.broken_image)
            }
        }
    }

    interface ClickListenerButton {
        fun onItemClicked(toDoEntry: ToDoEntry)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DetailTodoEntryBinding.inflate(
                LayoutInflater.from(
                    parent.context,
                )
            ),
            parent
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = getItem(position)

        holder.binding.todoButtonDeleteEntry.setOnClickListener {
            itemClickListenerButton.onItemClicked(current)
        }

        holder.bind(current)
    }


    companion
    object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ToDoEntry>() {

            override fun areItemsTheSame(
                oldItem: ToDoEntry,
                newItem: ToDoEntry,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ToDoEntry,
                newItem: ToDoEntry,
            ): Boolean {
                return oldItem.picture == newItem.picture
            }
        }
    }
}
