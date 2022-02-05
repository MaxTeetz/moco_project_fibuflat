package com.example.moco_project_fibuflat.activityGroup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.data.MoneyPoolEntry
import com.example.moco_project_fibuflat.databinding.MoneypoolPoolEntryBinding

class MoneyPoolAdapter(private val onItemClicked: (MoneyPoolEntry) -> Unit) :
    ListAdapter<MoneyPoolEntry, MoneyPoolAdapter.MoneyPoolViewHolder>(DiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyPoolViewHolder {
        return MoneyPoolViewHolder(
            MoneypoolPoolEntryBinding.inflate(
                LayoutInflater.from(
                    parent.context,
                )
            ),
            parent
        )
    }

    override fun onBindViewHolder(holder: MoneyPoolViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class MoneyPoolViewHolder(
        private var binding: MoneypoolPoolEntryBinding,
        private var parent: ViewGroup
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(moneyPoolEntry: MoneyPoolEntry) {

            binding.apply {
                userName.text = moneyPoolEntry.stringUser
                moneyGiven.text = parent.context.getString(
                    R.string.money_amount_in_euro,
                    moneyPoolEntry.moneyAmount.toString()
                )
                date.text = moneyPoolEntry.stringDate
            }
        }
    }

    companion
    object {
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
                return oldItem.moneyAmount == newItem.moneyAmount
            }
        }
    }

}
