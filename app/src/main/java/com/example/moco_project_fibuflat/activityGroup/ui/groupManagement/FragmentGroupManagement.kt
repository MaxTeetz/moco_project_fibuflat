package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewJoinRequestAdapter
import com.example.moco_project_fibuflat.data.repository.OftenNeededData
import com.example.moco_project_fibuflat.databinding.FragmentGroupManagementBinding

class FragmentGroupManagement : Fragment() {

    private var _binding: FragmentGroupManagementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroupManagementViewModel by viewModels()
    private val oftenNeededData: OftenNeededData by viewModels()

    private lateinit var requestRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as GroupActivity).supportActionBar?.title = "Group Management"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGroupManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestRecyclerView = binding.recyclerViewJoinRequests
        requestRecyclerView.layoutManager = LinearLayoutManager(this.context)
        requestRecyclerView.setHasFixedSize(true)

        viewModel.getUserData(oftenNeededData.dataBaseGroups)


        viewModel.requestListNew.observe(viewLifecycleOwner) {
            requestRecyclerView.adapter = RecyclerViewJoinRequestAdapter(viewModel, it)
            //when (viewModel.listCases) {
            //    ListCases.EMPTY -> requestRecyclerView.adapter = RecyclerViewJoinRequestAdapter(it)
            //    ListCases.ADDED -> requestRecyclerView.adapter?.notifyItemInserted(viewModel.index!!)
            //    ListCases.DELETED -> requestRecyclerView.adapter?.notifyItemRemoved(viewModel.index!!)
            //    else -> Log.d("adapterGroupManagement", "Error")
            //} //ToDo see corresponding viewModel
        }
    }
}