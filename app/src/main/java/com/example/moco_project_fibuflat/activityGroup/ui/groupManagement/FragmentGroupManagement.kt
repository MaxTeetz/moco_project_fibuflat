package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.activityGroup.adapter.GroupMembersAdapter
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewItemDecoration
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewJoinRequestAdapter
import com.example.moco_project_fibuflat.data.ListCases
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.repository.OftenNeededData
import com.example.moco_project_fibuflat.databinding.FragmentGroupManagementBinding

class FragmentGroupManagement : Fragment() {

    private var _binding: FragmentGroupManagementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroupManagementViewModel by viewModels()
    private lateinit var neededData: OftenNeededData
    private lateinit var adapterRequest: RecyclerViewJoinRequestAdapter
    private lateinit var adapterMembers: GroupMembersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        neededData = ViewModelProvider(requireActivity())[OftenNeededData::class.java]
        (activity as GroupActivity).supportActionBar?.title = "Group Management"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGroupManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setDataViewModel(neededData.dataBaseUsers,
            neededData.dataBaseGroups,
            neededData.group,
            neededData.user)

        viewModel.toast.observe(this.viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                "$it already in a group. Consider deleting the request",
                Toast.LENGTH_SHORT).show()
        }


        setAdapters()
        bindingRecyclerViewRequests()
        requestObserver()

        viewModel.getUserData()
    }


    private fun setAdapters() {
        adapterRequest = RecyclerViewJoinRequestAdapter(
            object : RecyclerViewJoinRequestAdapter.ClickListenerAccept {
                override fun onItemClicked(openRequestGroup: OpenRequestGroup) {
                    getUserAccept(openRequestGroup)
                }
            },
            object : RecyclerViewJoinRequestAdapter.ClickListenerDecline {
                override fun onItemClicked(openRequestGroup: OpenRequestGroup) {
                    getUserDecline(openRequestGroup)
                }
            })
    }

    private fun bindingRecyclerViewRequests() {
        binding.recyclerViewJoinRequests.adapter = adapterRequest

        binding.recyclerViewJoinRequests.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerViewJoinRequests.addItemDecoration(
            RecyclerViewItemDecoration(
                this.requireContext(),
                R.drawable.divider_shape))
    }

    private fun requestObserver() {
        viewModel.requestListNew.observe(this.viewLifecycleOwner) { it ->
            it.let {
                adapterRequest.submitList(it)

                when (viewModel.listCases) {
                    ListCases.EMPTY -> {
                        adapterRequest.submitList(it)
                        adapterRequest.notifyItemRangeChanged(0, it.size - 1)
                    }
                    ListCases.DELETED -> adapterRequest.notifyItemRemoved(viewModel.index!!)

                    ListCases.ADDED -> adapterRequest.notifyItemInserted(viewModel.index!!)

                    null -> { //should actually never happen. But just in case, reload the whole list
                        adapterRequest.notifyDataSetChanged()
                        Log.d("adapter", "Error")
                    }
                }
            }
        }
    }

    private fun getUserAccept(openRequestGroup: OpenRequestGroup) {
        viewModel.acceptUser(openRequestGroup)
    }

    private fun getUserDecline(openRequestGroup: OpenRequestGroup) {
        viewModel.declineUser(openRequestGroup)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeListener()
    }
}