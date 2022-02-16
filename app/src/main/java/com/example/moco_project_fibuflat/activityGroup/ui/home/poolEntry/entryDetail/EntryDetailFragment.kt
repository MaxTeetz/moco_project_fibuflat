package com.example.moco_project_fibuflat.activityGroup.ui.home.poolEntry.entryDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.data.MoneyPoolEntry
import com.example.moco_project_fibuflat.data.repository.OftenNeededData
import com.example.moco_project_fibuflat.databinding.EntryDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class EntryDetailFragment : Fragment() {
    private val navigationArgs: EntryDetailFragmentArgs by navArgs()

    private var _binding: EntryDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EntryDetailViewModel by viewModels()
    private lateinit var neededData: OftenNeededData

    private val coroutine1 = Job()
    private val coroutine2 = Job()
    private val coroutineScope1 = CoroutineScope(coroutine1 + Dispatchers.Main)
    private val coroutineScope2 = CoroutineScope(coroutine2 + Dispatchers.Main)

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        neededData = ViewModelProvider(requireActivity())[OftenNeededData::class.java]
        _binding = EntryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Entry: ${navigationArgs.dateSend}"
        val action: NavDirections = EntryDetailFragmentDirections.actionEntryDetailToNavHome()
        val id = navigationArgs.entryId

        var moneyPoolEntry: MoneyPoolEntry

        coroutineScope1.launch {
            moneyPoolEntry = MoneyPoolEntry(null, navigationArgs.username, navigationArgs.amount, null, navigationArgs.message)
            bind(moneyPoolEntry)
        }

        binding.deleteEntry.setOnClickListener {
            coroutineScope2.launch {
                viewModel.deleteEntry(
                    id,
                    neededData.group.value!!.groupId!!,
                    neededData.dataBaseGroups)
            }
            this.findNavController().navigate(action)
        }

        binding.cancel.setOnClickListener {
            this.findNavController().navigate(action)
        }
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