package com.example.moco_project_fibuflat.activityGroup.ui.toDo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewItemDecoration
import com.example.moco_project_fibuflat.activityGroup.adapter.ToDoAdapter
import com.example.moco_project_fibuflat.data.ListCase
import com.example.moco_project_fibuflat.databinding.FragmentTodoBinding
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ToDoFragment : Fragment() {

    private lateinit var viewModel: ToDoViewModel
    private lateinit var neededData: OftenNeededData
    private lateinit var adapterToDo: ToDoAdapter

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private val coroutine1 = Job()
    private val coroutineScope1 = CoroutineScope(coroutine1 + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[ToDoViewModel::class.java]
        neededData = ViewModelProvider(requireActivity())[OftenNeededData::class.java]
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setDataViewModel(
            neededData.dataBaseUsers,
            neededData.dataBaseGroups,
            neededData.group,
            neededData.user)

        coroutineScope1.launch {
            viewModel.getEntries()
        }

        setAdapter()
        bindingRecyclerViewRequests()
        requestObserver()

        binding.addTodoEntryButton.setOnClickListener {
            val action = ToDoFragmentDirections.actionNavTodoListToAddToDoEntryFragament()
            this.findNavController().navigate(action)
        }
    }

    private fun setAdapter() {
        adapterToDo = ToDoAdapter()
    }

    private fun bindingRecyclerViewRequests() {
        binding.recyclerViewTodoList.adapter = adapterToDo
        binding.recyclerViewTodoList.addItemDecoration(
            RecyclerViewItemDecoration(
                this.requireContext(),
                R.drawable.divider_shape))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun requestObserver() {
        viewModel.allToDoEntries.observe(this.viewLifecycleOwner) { it ->
            it.let {
                adapterToDo.submitList(it)

                when (viewModel.listCase) {
                    ListCase.EMPTY -> adapterToDo.submitList(it)
                    ListCase.DELETED -> adapterToDo.notifyItemRemoved(viewModel.index!!)
                    ListCase.ADDED -> adapterToDo.notifyItemInserted(viewModel.index!!)
                    null -> { //should actually never happen. But just in case, reload the whole list
                        adapterToDo.notifyDataSetChanged()
                        Log.d("adapter", "Error")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeListeners()
    }
}