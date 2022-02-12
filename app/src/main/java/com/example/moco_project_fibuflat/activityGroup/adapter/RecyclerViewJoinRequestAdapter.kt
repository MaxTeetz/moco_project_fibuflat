package com.example.moco_project_fibuflat.activityGroup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.databinding.RecyclerViewJoinRequestsEntryBinding

class RecyclerViewJoinRequestAdapter(
    private val clickListener: (OpenRequestGroup) -> Unit
    //private val clickListenerDecline: (OpenRequestGroup) -> Unit,
) :
    ListAdapter<OpenRequestGroup, RecyclerViewJoinRequestAdapter.MyViewHolder>(DiffCallback) {


    class MyViewHolder(
        val binding: RecyclerViewJoinRequestsEntryBinding,
        private var parent: ViewGroup
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(openRequestGroup: OpenRequestGroup) {
            binding.apply {
                usernameRequestToGroup.text = openRequestGroup.username
                declineUser.setOnClickListener {

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewJoinRequestAdapter.MyViewHolder {
        return RecyclerViewJoinRequestAdapter.MyViewHolder(
            RecyclerViewJoinRequestsEntryBinding.inflate(
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
            clickListener(current)
        }

        holder.binding.declineUser.setOnClickListener {
            clickListener(current)
        }
        holder.itemView.setOnClickListener {
            clickListener(current)
        }
        holder.bind(current)
    }


/*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
    val itemView = LayoutInflater.from(parent.context)
        .inflate(R.layout.recycler_view_join_requests_entry, parent, false)
    return MyViewHolder(itemView)
}

override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
    val currentItem = getItem(position)
    holder.buttonAcceptUser.setOnClickListener {
        clickListenerAccept.onItemClicked(position)
    }
    holder.buttonDeclineUser.setOnClickListener {
        clickListenerDecline.onItemClicked(position)
    }
    holder.bind(currentItem)
}*/

//fun getItem(position: Int) : OpenRequestGroup = userList[position]

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
