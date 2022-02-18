package com.example.moco_project_fibuflat.activitySelectGroup.ui.joinGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.moco_project_fibuflat.databinding.FragmentJoinGroupBinding
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class JoinGroupFragment : Fragment() {

    private var _binding: FragmentJoinGroupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JoinGroupViewModel by viewModels()
    private lateinit var neededData: OftenNeededData
    private val coroutine1 = Job()
    private val coroutineScope1 = CoroutineScope(coroutine1 + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        neededData = ViewModelProvider(requireActivity())[OftenNeededData::class.java]
        _binding = FragmentJoinGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.success.observe(this.viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.joinGroupButton.setOnClickListener {
            checkAllEntries()
        }
    }

    private fun checkAllEntries() {
        val name: String = binding.findGroupInput.text.toString()
        val id: String = binding.idInput.text.toString()
        if (name.isBlank() || id.isBlank())
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        else
            coroutineScope1.launch {
                viewModel.joinGroup(binding.findGroupInput.text.toString(),
                    binding.idInput.text.toString(),
                    neededData.user.value!!,
                    neededData.dataBaseUsers,
                    neededData.dataBaseGroups)
            }
    }
}