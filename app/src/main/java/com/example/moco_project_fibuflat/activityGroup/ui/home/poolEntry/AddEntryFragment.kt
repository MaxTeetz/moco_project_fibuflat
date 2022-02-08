package com.example.moco_project_fibuflat.activityGroup.ui.home.poolEntry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.activityGroup.data.MoneyPoolEntry
import com.example.moco_project_fibuflat.activityGroup.ui.home.HomeViewModel
import com.example.moco_project_fibuflat.databinding.FragmentAddEntryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class AddEntryFragment : Fragment() {

    private val viewModel: AddEntryViewModel by activityViewModels()
    private val viewModelHome: HomeViewModel by activityViewModels()
    private var _binding: FragmentAddEntryBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("addEntryFragment", "onCreateView")
        // Inflate the layout for this fragment
        _binding = FragmentAddEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("addEntryFragment", "onViewCreated")

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
                id = UUID.randomUUID().toString(),
                "Max Mustermann",
                binding.moneyAmount.text.toString().toInt(),
                currentDate,
                binding.message.text.toString()
            )
            addEntryToDB(moneyPoolEntry)

            viewModelHome.addItem( //ToDo get from database
                moneyPoolEntry
            )
            viewModelHome.moneyGoalSetCurrent()
            val action = AddEntryFragmentDirections.actionAddEntryFragmentToNavHome()
            findNavController().navigate(action)
        }
    }

    private fun addEntryToDB(moneyPoolEntry: MoneyPoolEntry) {
        val intent = activity?.intent?.extras?.getString("groupID")
        //Log.d("homeFragment", intent.getStringExtra("groupID")!!)

        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Groups")
        database.child(intent!!).child("moneyPoolEntry").setValue(moneyPoolEntry)
    }
}