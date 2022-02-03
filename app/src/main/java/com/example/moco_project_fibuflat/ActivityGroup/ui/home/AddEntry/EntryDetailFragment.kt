package com.example.moco_project_fibuflat.ActivityGroup.ui.home.AddEntry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.Data.MoneyPoolEntry
import com.example.moco_project_fibuflat.ActivityGroup.ui.home.HomeViewModel
import com.example.moco_project_fibuflat.databinding.EntryDetailBinding
import java.util.*


class EntryDetailFragment : Fragment() {
    private val navigationArgs: EntryDetailFragmentArgs by navArgs()

    private var _binding: EntryDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EntryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.entryId
        bind(viewModel.getEntry(id)) //to display in detail

        binding.deleteEntry.setOnClickListener {
            deleteEntry(id)
        }
    }

    fun deleteEntry(id: UUID) {
        Log.d("viewModel", "detail: " + id)
        viewModel.deleteEntry(id)
        this.findNavController().navigateUp()

    }

    private fun bind(entryMoneyPoolEntry: MoneyPoolEntry) {
        binding.apply {
            username.text = entryMoneyPoolEntry.stringUserId
            moneyGiven.text = entryMoneyPoolEntry.stringMoneyId.toString()
            message.text = entryMoneyPoolEntry.stringInfo
        }
    }
}