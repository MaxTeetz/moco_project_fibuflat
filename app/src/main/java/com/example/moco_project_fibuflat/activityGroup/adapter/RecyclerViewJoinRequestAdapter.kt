package com.example.moco_project_fibuflat.activityGroup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.databinding.DetailJoinRequestsEntryBinding

class RecyclerViewJoinRequestAdapter(
    private val itemClickListenerAccept: ClickListenerAccept,
    private val itemClickListenerDecline: ClickListenerDecline,
) :
    ListAdapter<OpenRequestGroup, RecyclerViewJoinRequestAdapter.MyViewHolder>(DiffCallback) {


    interface ClickListenerAccept {
        fun onItemClicked(openRequestGroup: OpenRequestGroup)
    }

    interface ClickListenerDecline{
        fun onItemClicked(openRequestGroup: OpenRequestGroup)
    }

    class MyViewHolder(
        val binding: DetailJoinRequestsEntryBinding,
        private var parent: ViewGroup,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(openRequestGroup: OpenRequestGroup) {
            binding.apply {
                usernameRequestToGroup.text = openRequestGroup.username
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DetailJoinRequestsEntryBinding.inflate(
                LayoutInflater.from(
                    parent.context,
                )
            ),
            parent
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val current = getItem(position)

        holder.binding.acceptUser.setOnClickListener {
            itemClickListenerAccept.onItemClicked(current)
        }

        holder.binding.declineUser.setOnClickListener {
            itemClickListenerDecline.onItemClicked(current)
        }
        holder.bind(current)
    }

    companion
    object {
        private val DiffCallback = object : DiffUtil.ItemCallback<OpenRequestGroup>() {

            override fun areItemsTheSame(
                oldItem: OpenRequestGroup,
                newItem: OpenRequestGroup,
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: OpenRequestGroup,
                newItem: OpenRequestGroup,
            ): Boolean {
                return oldItem.userID == newItem.userID
            }
        }
    }
}
