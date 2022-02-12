package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewItemDecoration
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewJoinRequestAdapter
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.repository.OftenNeededData
import com.example.moco_project_fibuflat.databinding.FragmentGroupManagementBinding

class FragmentGroupManagement : Fragment() {

    private var _binding: FragmentGroupManagementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroupManagementViewModel by viewModels()
    private val oftenNeededData: OftenNeededData by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as GroupActivity).supportActionBar?.title = "Group Management"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGroupManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindRecyclerView()
    }

    private fun bindRecyclerView() {
        val adapter = RecyclerViewJoinRequestAdapter {
            //ToDo just the click part. The notifyOnChanged ( viewModel) could be used now
            Log.d("fragmentGroupManagement", it.toString())
        }

        binding.recyclerViewJoinRequests.adapter = adapter

        viewModel.getUserData(oftenNeededData.dataBaseGroups)

        viewModel.requestListNew.observe(this.viewLifecycleOwner) { it ->
            it.let {
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        }

        binding.recyclerViewJoinRequests.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerViewJoinRequests.addItemDecoration(
            RecyclerViewItemDecoration(
                this.requireContext(),
                R.drawable.divider_shape
            )
        )
    }


    private fun getUserAccept(openRequestGroup: OpenRequestGroup) {
        Log.d("fragmentGroupManagement", "Accept: $openRequestGroup")
    }

    private fun getUserDecline(openRequestGroup: OpenRequestGroup) {
        Log.d("fragmentGroupManagement", "Decline: $openRequestGroup")
    }
}