package com.example.moco_project_fibuflat.activityGroup.ui.home.poolEntry.addEntry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.data.MoneyPoolEntry
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData
import com.example.moco_project_fibuflat.databinding.FragmentAddEntryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*


class AddEntryFragment : Fragment() {

    private val viewModel: AddEntryViewModel by activityViewModels()
    private lateinit var neededData: OftenNeededData
    private var _binding: FragmentAddEntryBinding? = null
    private val binding get() = _binding!!
    private val coroutine1 = Job()
    private val coroutineScope1 = CoroutineScope(coroutine1 + Dispatchers.Main)

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
        Log.d("addEntryFragment", "onCreateView")
        // Inflate the layout for this fragment
        _binding = FragmentAddEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("addEntryFragment", "onViewCreated")

        viewModel.setData(
            neededData.user.value!!,
            neededData.group.value!!,
            neededData.dataBaseGroups)

        binding.addEntry.setOnClickListener {
            addNewEntry()
        }
        binding.cancel.setOnClickListener {
            val action = AddEntryFragmentDirections.actionAddEntryFragmentToNavHome()
            findNavController().navigate(action)
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.moneyAmount.text.toString(),
            binding.message.text.toString()
        )
    }

    private fun addNewEntry() {
        if (isEntryValid()) {

            coroutineScope1.launch {
                viewModel.addEntryToDB(MoneyPoolEntry(
                    UUID.randomUUID().toString(),
                    neededData.user.value!!.username!!,
                    binding.moneyAmount.text.toString().toInt(),
                    viewModel.getCurrentDate(),
                    binding.message.text.toString()
                ))
            }
            val action = AddEntryFragmentDirections.actionAddEntryFragmentToNavHome()
            findNavController().navigate(action)
        }
    }
}