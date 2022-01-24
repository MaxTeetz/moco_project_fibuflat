package com.example.moco_project_fibuflat.ActivityGroup.ui.home

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.Data.MoneyPoolEntry
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.databinding.FragmentAddEntryBinding


class AddEntryFragment : Fragment() {
    lateinit var moneyPoolEntry: MoneyPoolEntry

    private val viewModel: AddEntryViewModel by activityViewModels()
    private val viewModelHome: HomeViewModel by activityViewModels()
    private var _binding: FragmentAddEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.moneyAmount.text.toString()
        )
    }

    private fun addNewEntry() {
        if (isEntryValid()) {
            val moneyPoolEntry =
                MoneyPoolEntry(R.string.max_mustermann, binding.moneyAmount.text.toString().toInt())
            viewModelHome.addEntry(
                moneyPoolEntry
            ) //ToDo get username from logged in user
            val action = AddEntryFragmentDirections.actionAddEntryFragmentToNavHome()
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addEntry.setOnClickListener {
            addNewEntry()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}