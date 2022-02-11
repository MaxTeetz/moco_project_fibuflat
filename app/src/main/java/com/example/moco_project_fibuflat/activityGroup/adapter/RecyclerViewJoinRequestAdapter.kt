package com.example.moco_project_fibuflat.activityGroup.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.OpenRequestGroup

class RecyclerViewJoinRequestAdapter(private val userList: ArrayList<OpenRequestGroup>) :
    RecyclerView.Adapter<RecyclerViewJoinRequestAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buttonAccept: Button = itemView.findViewById(R.id.accept_user)
        val buttonDecline: Button = itemView.findViewById(R.id.decline_user)
        val textView: TextView = itemView.findViewById(R.id.username_request_to_group)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_join_requests_entry, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        holder.textView.text = currentItem.username
        holder.buttonAccept.setOnClickListener { accept() }
        holder.buttonDecline.setOnClickListener { decline() }
    }

    override fun getItemCount() = userList.size


    private fun accept() {
        Log.d("adapter", "accept")
    }

    private fun decline() {
        Log.d("adapter", "decline")
    }

    fun dataChanged(index: Int){
        notifyItemRemoved(index)
    }

}