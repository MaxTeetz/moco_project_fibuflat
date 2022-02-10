package com.example.moco_project_fibuflat.activitySelectGroup.ui.selectGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.activitySelectGroup.SelectGroupActivity
import com.example.moco_project_fibuflat.databinding.FragmentSelectGroupBinding

class SelectGroupFragment : Fragment() {

    private var _binding: FragmentSelectGroupBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        (activity as SelectGroupActivity).supportActionBar?.title = "Select Option"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as SelectGroupActivity).supportActionBar?.title = "Select Group"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.joinGroup.setOnClickListener {
            val action =
                SelectGroupFragmentDirections.actionSelectGroupFragmentToJoinGroupFragment()
            this.findNavController().navigate(action)
        }

        binding.createGroup.setOnClickListener {
            val action =
                SelectGroupFragmentDirections.actionSelectGroupFragmentToCreateGroupFragment()
            this.findNavController().navigate(action)
        }
    }
}