package com.example.moco_project_fibuflat.activityGroup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.OpenRequestGroup

class RecyclerViewJoinRequestAdapter(
    private val userList: ArrayList<OpenRequestGroup>,
    private val clickListenerAccept: AcceptUser,
    private val clickListenerDecline: DeclineUser
    //private val clickListenerDecline: (openRequestGroup: OpenRequestGroup) -> Unit
) :
    RecyclerView.Adapter<RecyclerViewJoinRequestAdapter.MyViewHolder>() {


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonAcceptUser: Button = view.findViewById(R.id.accept_user)
        val buttonDeclineUser: Button = view.findViewById(R.id.decline_user)
        val username : TextView = view.findViewById(R.id.username_request_to_group)
        fun bind(openRequestGroup: OpenRequestGroup) {
            username.text = openRequestGroup.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_join_requests_entry, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.buttonAcceptUser.setOnClickListener {
            clickListenerAccept.onItemClicked(position)
        }
        holder.buttonDeclineUser.setOnClickListener {
            clickListenerDecline.onItemClicked(position)
        }
        holder.bind(currentItem)
    }

    override fun getItemCount() = userList.size

    interface AcceptUser {
        fun onItemClicked(position: Int)
    }

    interface DeclineUser{
        fun onItemClicked(position: Int)
    }

    fun getItem(position: Int) : OpenRequestGroup = userList[position]

}