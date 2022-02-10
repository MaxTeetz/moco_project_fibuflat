package com.example.moco_project_fibuflat.activityGroup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.OpenRequestGroup

class RecyclerViewJoinRequestAdapter(private val userList: ArrayList<OpenRequestGroup>) :
    RecyclerView.Adapter<RecyclerViewJoinRequestAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_join_requests_entry, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.id.text = currentItem.userID
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.username_request_to_group)
    }
}