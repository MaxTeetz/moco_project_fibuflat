package com.example.moco_project_fibuflat.activityGroup.ui.home.poolEntry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.activityGroup.data.MoneyPoolEntry
import com.example.moco_project_fibuflat.activityGroup.ui.home.HomeViewModel
import com.example.moco_project_fibuflat.databinding.EntryDetailBinding
import java.util.*


class EntryDetailFragment : Fragment() {
    private val navigationArgs: EntryDetailFragmentArgs by navArgs()

    private var _binding: EntryDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()
        (activity as GroupActivity).supportActionBar?.title = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EntryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.entryId
        bind(viewModel.getEntry(id))

        binding.deleteEntry.setOnClickListener {
            deleteEntry(id)
        }

        binding.cancel.setOnClickListener {
            this.findNavController().navigateUp()
        }
    }

    private fun deleteEntry(id: UUID) {
        viewModel.deleteEntry(id)
        this.findNavController().navigateUp()

    }

    private fun bind(entryMoneyPoolEntry: MoneyPoolEntry) {
        binding.apply {
            username.text = entryMoneyPoolEntry.stringUser
            moneyGiven.text = view?.context?.getString(
                R.string.money_amount_in_euro,
                entryMoneyPoolEntry.moneyAmount.toString()
            )
            message.text = entryMoneyPoolEntry.stringInfo
        }
    }
}