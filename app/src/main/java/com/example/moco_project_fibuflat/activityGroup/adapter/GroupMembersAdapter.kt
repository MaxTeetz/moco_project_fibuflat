package com.example.moco_project_fibuflat.activityGroup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.data.User
import com.example.moco_project_fibuflat.databinding.EntryUserBinding

class GroupMembersAdapter() :
    ListAdapter<User, GroupMembersAdapter.MyViewHolder>(DiffCallback) {

    class MyViewHolder(
        val binding: EntryUserBinding,
        private var parent: ViewGroup,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                userName.text = user.username
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            EntryUserBinding.inflate(
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
        private val DiffCallback = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(
                oldItem: User,
                newItem: User,
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User,
            ): Boolean {
                return oldItem.userID == newItem.userID
            }
        }
    }
}
