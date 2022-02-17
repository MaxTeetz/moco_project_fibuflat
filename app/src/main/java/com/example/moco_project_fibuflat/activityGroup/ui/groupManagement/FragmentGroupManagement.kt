package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.adapter.GroupMembersAdapter
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewItemDecoration
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewJoinRequestAdapter
import com.example.moco_project_fibuflat.data.ListCase
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.repository.OftenNeededData
import com.example.moco_project_fibuflat.databinding.FragmentGroupManagementBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FragmentGroupManagement : Fragment() {

    private lateinit var viewModel: GroupManagementViewModel
    private lateinit var neededData: OftenNeededData
    private lateinit var adapterRequest: RecyclerViewJoinRequestAdapter
    private lateinit var adapterMembers: GroupMembersAdapter

    private var _binding: FragmentGroupManagementBinding? = null
    private val binding get() = _binding!!

    private val coroutine1 = Job()
    private val coroutine2 = Job()
    private val coroutineScope1 = CoroutineScope(coroutine1 + Dispatchers.Main)
    private val coroutineScope2 = CoroutineScope(coroutine2 + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[GroupManagementViewModel::class.java]
        neededData = ViewModelProvider(requireActivity())[OftenNeededData::class.java]
        _binding = FragmentGroupManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setDataViewModel(
            neededData.dataBaseUsers,
            neededData.dataBaseGroups,
            neededData.group,
            neededData.user)

        coroutineScope1.launch { //toDo
            viewModel.getRequests()
        }

        coroutineScope2.launch {
            viewModel.getGroupMembers()
        }

        viewModel.toast.observe(this.viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                "$it already in a group. Consider deleting the request",
                Toast.LENGTH_SHORT).show()
        }

        setAdapters()
        bindingRecyclerViewRequests()
        bindingRecyclerViewMembers()
        requestObserver()
        memberObserver()
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

        adapterMembers = GroupMembersAdapter()
    }

    private fun bindingRecyclerViewRequests() {
        binding.recyclerViewJoinRequests.adapter = adapterRequest

        binding.recyclerViewJoinRequests.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerViewJoinRequests.addItemDecoration(
            RecyclerViewItemDecoration(
                this.requireContext(),
                R.drawable.divider_shape))
    }

    private fun bindingRecyclerViewMembers() {
        binding.recyclerViewMembers.adapter = adapterMembers

        binding.recyclerViewMembers.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerViewMembers.addItemDecoration(
            RecyclerViewItemDecoration(
                this.requireContext(),
                R.drawable.divider_shape))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun requestObserver() {
        viewModel.requestListNew.observe(this.viewLifecycleOwner) { it ->
            it.let {
                adapterRequest.submitList(it)

                when (viewModel.listCaseRequest) {
                    ListCase.EMPTY -> adapterRequest.submitList(it)
                    ListCase.DELETED -> adapterRequest.notifyItemRemoved(viewModel.indexRequest!!)
                    ListCase.ADDED -> adapterRequest.notifyItemInserted(viewModel.indexRequest!!)
                    null -> { //impossible to reach that case but need an else branch. Reload list in this case, because otherwise a crash occurs.
                        adapterRequest.notifyDataSetChanged()
                        Log.d("adapter", "Error")
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun memberObserver() {
        viewModel.memberListNew.observe(this.viewLifecycleOwner) { it ->
            it.let {
                adapterMembers.submitList(it)

                when (viewModel.listCaseMember) {
                    ListCase.EMPTY -> adapterMembers.submitList(it)
                    ListCase.DELETED -> adapterMembers.notifyItemRemoved(viewModel.indexMember!!)
                    ListCase.ADDED -> adapterMembers.notifyItemInserted(viewModel.indexMember!!)
                    null -> {
                        adapterMembers.notifyDataSetChanged()
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
        viewModel.removeListeners()
        Log.d("groupManagementFragment", "destroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("groupManagementFragment", "destroyed")
    }
}