package com.example.moco_project_fibuflat.activityGroup.ui.groupInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.data.Connectivity
import com.example.moco_project_fibuflat.databinding.FragmentGroupInfoBinding
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class FragmentGroupInfo : Fragment() {
    private var _binding: FragmentGroupInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GroupInfoViewModel
    private lateinit var neededData: OftenNeededData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        viewModel = ViewModelProvider(this)[GroupInfoViewModel::class.java]
        neededData = ViewModelProvider(requireActivity())[OftenNeededData::class.java]
        _binding = FragmentGroupInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.infoGroupName.text = neededData.group.value!!.groupName
        binding.infoGroupId.text = neededData.group.value!!.groupId!!.substring(0, 4)

        binding.infoLeaveGroup.setOnClickListener {
            if(neededData.connectivityStatus.value != Connectivity.OFFLINE)
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.leave_group_confirm))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                leaveGroup()
            }
            .show()
    }

    private fun leaveGroup() {
        lifecycleScope.launch {
            viewModel.leaveGroup(
                neededData.user.value!!,
                neededData.group.value!!,
                neededData.dataBaseUsers,
                neededData.dataBaseGroups)
            (activity as GroupActivity).changeToSelectGroup()
        }
    }
}