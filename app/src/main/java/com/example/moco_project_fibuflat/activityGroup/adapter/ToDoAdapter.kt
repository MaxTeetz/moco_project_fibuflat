package com.example.moco_project_fibuflat.activityGroup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.ToDoEntry
import com.example.moco_project_fibuflat.databinding.DetailTodoEntryBinding
import com.example.moco_project_fibuflat.helperClasses.GetStoragePicture

class ToDoAdapter() :
    ListAdapter<ToDoEntry, ToDoAdapter.MyViewHolder>(DiffCallback) {

    class MyViewHolder(
        val binding: DetailTodoEntryBinding,
        private var parent: ViewGroup,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoEntry: ToDoEntry) {
            binding.apply {
                todoEntryTextName.text = toDoEntry.name
                todoEntryTextTask.text = toDoEntry.message

                if (toDoEntry.pictureAdded != null) {
                    todoEntryPicture.setImageResource(R.drawable.loading_image)
                    GetStoragePicture(toDoEntry.pictureAdded!!) { bitmap ->
                        if (bitmap != null) {
                            todoEntryPicture.setImageBitmap(bitmap)
                        }else
                            todoEntryPicture.setImageResource(R.drawable.broken_image)
                    }
                } else
                    todoEntryPicture.setImageResource(R.drawable.todo_image_select)
            }
        }
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
        holder.bind(current)
    }

    companion
    object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ToDoEntry>() {

            override fun areItemsTheSame(
                oldItem: ToDoEntry,
                newItem: ToDoEntry,
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: ToDoEntry,
                newItem: ToDoEntry,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
