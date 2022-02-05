package com.example.moco_project_fibuflat.activityGroup.ui.home.AddEntry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.activityGroup.data.MoneyPoolEntry
import com.example.moco_project_fibuflat.activityGroup.ui.home.HomeViewModel
import com.example.moco_project_fibuflat.databinding.FragmentAddEntryBinding
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*


class AddEntryFragment : Fragment() {

    private val viewModel: AddEntryViewModel by activityViewModels()
    private val viewModelHome: HomeViewModel by activityViewModels()
    private var _binding: FragmentAddEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

            val sdf = SimpleDateFormat("dd/M/yyyy\nhh:mm:ss")
            val currentDate = sdf.format(Date())

            val moneyPoolEntry = MoneyPoolEntry(
                id = UUID.randomUUID(),
                FirebaseAuth.getInstance().currentUser!!.uid,
                binding.moneyAmount.text.toString().toInt(),
                currentDate,
                binding.message.text.toString()
            )
            viewModelHome.addItem(
                moneyPoolEntry
            )
            val action = AddEntryFragmentDirections.actionAddEntryFragmentToNavHome()
            findNavController().navigate(action)
        }
    }
}