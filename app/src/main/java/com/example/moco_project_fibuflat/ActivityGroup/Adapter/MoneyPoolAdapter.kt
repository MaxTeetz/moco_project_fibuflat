package com.example.moco_project_fibuflat.ActivityGroup.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.Data.MoneyPoolEntry
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.databinding.MoneypoolPoolEntryBinding

class MoneyPoolAdapter(private val onItemClicked: (MoneyPoolEntry) -> Unit) :
    ListAdapter<MoneyPoolEntry, MoneyPoolAdapter.MoneyPoolViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyPoolViewHolder {
        return MoneyPoolViewHolder(
            parent,
            MoneypoolPoolEntryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: MoneyPoolViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class MoneyPoolViewHolder(private var parent: ViewGroup,private var binding: MoneypoolPoolEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(moneyPoolEntry: MoneyPoolEntry) {
            binding.apply {
                userName.text = parent.context.getString(R.string.max_mustermann)//ToDo get Username from logged in User
                moneyGiven.text = moneyPoolEntry.stringMoneyId.toString()
            }
        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MoneyPoolEntry>() {

            override fun areItemsTheSame(
                oldItem: MoneyPoolEntry,
                newItem: MoneyPoolEntry
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: MoneyPoolEntry,
                newItem: MoneyPoolEntry
            ): Boolean {
                return oldItem.stringMoneyId == newItem.stringMoneyId
            }
        }
    }

}