package com.example.moco_project_fibuflat.activitySelectGroup.ui.createGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activitySelectGroup.SelectGroupActivity
import com.example.moco_project_fibuflat.databinding.FragmentCreateGroupBinding
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData

class CreateGroupFragment : Fragment() {

    private val viewModel: CreateGroupViewModel by viewModels()

    private var _binding: FragmentCreateGroupBinding? = null
    private val binding get() = _binding!!

    private lateinit var neededData: OftenNeededData
    private lateinit var groupName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as SelectGroupActivity).supportActionBar?.title = "Create Group"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        neededData = ViewModelProvider(requireActivity())[OftenNeededData::class.java]
        _binding = FragmentCreateGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createGroupButton.setOnClickListener {
            createGroup()
        }
    }

    private fun createGroup() {
        groupName = binding.createGroupInput.text.toString()
        if (viewModel.checkIfFilled(groupName)) {
            setErrorTextFieldGroup(true)
        } else {
            setErrorTextFieldGroup(false)
            viewModel.createGroup(groupName, neededData.user.value!!, neededData.dataBaseUsers,neededData.dataBaseGroups)
        }
    }

    private fun setErrorTextFieldGroup(error: Boolean) {
        if (error) {
            binding.createGroupLabel.isErrorEnabled = true
            binding.createGroupLabel.error = getString(R.string.emptyGroupName)
        } else {
            binding.createGroupLabel.isErrorEnabled = false
        }
    }
}