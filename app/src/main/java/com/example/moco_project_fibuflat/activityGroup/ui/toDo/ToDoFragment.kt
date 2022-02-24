package com.example.moco_project_fibuflat.activityGroup.ui.toDo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moco_project_fibuflat.activityGroup.adapter.ToDoAdapter
import com.example.moco_project_fibuflat.data.ListCase
import com.example.moco_project_fibuflat.data.ToDoEntry
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
        viewModel =
            ViewModelProvider(requireActivity())[ToDoViewModel::class.java] //bind to activity, so data isn't loaded multiple times
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

        if(viewModel.listCase.value == null)
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
        adapterToDo = ToDoAdapter(object : ToDoAdapter.ClickListenerButton {
            override fun onItemClicked(toDoEntry: ToDoEntry) {
                lifecycleScope.launch {
                    viewModel.deleteItem(toDoEntry)
                }
            }
        })
    }

    private fun bindingRecyclerViewRequests() {
        binding.recyclerViewTodoList.adapter = adapterToDo
        binding.recyclerViewTodoList.layoutManager = GridLayoutManager(this.context, 1)
        binding.recyclerViewTodoList.setHasFixedSize(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun requestObserver() {
        if(viewModel.listCase.value != null)
            adapterToDo.submitList(viewModel.allToDoEntries)

        viewModel.listCase.observe(this.viewLifecycleOwner) { it ->
            it.let {
                when (it) {
                    ListCase.EMPTY -> {
                        adapterToDo.submitList(viewModel.allToDoEntries)
                        Log.d("todo", "empty + ${viewModel.index}")

                    }
                    ListCase.DELETED -> {
                        adapterToDo.notifyItemRemoved(viewModel.index!!)
                        Log.d("todo", "deleted + ${viewModel.index}")
                    }
                    ListCase.ADDED -> {
                        adapterToDo.notifyItemInserted(viewModel.index!!)
                        Log.d("todo", "added + ${viewModel.index}")
                    }
                    ListCase.CHANGED -> {
                        adapterToDo.notifyItemChanged(viewModel.indexChanged!!,
                            viewModel.allToDoEntries[viewModel.indexChanged!!].picture)
                        Log.d("todo", "changed + ${viewModel.indexChanged}")
                    }
                    else -> {
                        adapterToDo.notifyDataSetChanged()
                        Log.d("adapter", "Error")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.resetListCase()
    }
}