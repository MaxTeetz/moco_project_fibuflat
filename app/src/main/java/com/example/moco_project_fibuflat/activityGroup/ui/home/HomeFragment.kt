package com.example.moco_project_fibuflat.activityGroup.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.adapter.MoneyPoolAdapter
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewItemDecoration
import com.example.moco_project_fibuflat.data.ListCase
import com.example.moco_project_fibuflat.data.repository.OftenNeededData
import com.example.moco_project_fibuflat.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var neededData: OftenNeededData
    private lateinit var adapterEntry: MoneyPoolAdapter
    private val coroutine1 = Job()
    private val coroutineScope1 = CoroutineScope(coroutine1 + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        neededData = ViewModelProvider(requireActivity())[OftenNeededData::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        binding.addEntry.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToAddEntryFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun setAdapter() {
        adapterEntry = MoneyPoolAdapter {
            val action = HomeFragmentDirections.actionNavHomeToEntryDetail(it.id!!,
                it.stringUser!!,
                it.moneyAmount!!,
                it.stringInfo!!,
                it.stringDate!!)
            this.findNavController().navigate(action)
        }
    }

    private fun bindingRecyclerViewRequests() {
        binding.recyclerViewMoneyPoolEntry.adapter = adapterEntry

        binding.recyclerViewMoneyPoolEntry.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerViewMoneyPoolEntry.addItemDecoration(
            RecyclerViewItemDecoration(
                this.requireContext(),
                R.drawable.divider_shape))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun requestObserver() {
        viewModel.allMoneyEntries.observe(this.viewLifecycleOwner) { it ->
            it.let {
                adapterEntry.submitList(it)

                when (viewModel.listCase) {
                    ListCase.EMPTY -> adapterEntry.submitList(it)
                    ListCase.DELETED -> adapterEntry.notifyItemRemoved(viewModel.index!!)
                    ListCase.ADDED -> adapterEntry.notifyItemInserted(viewModel.index!!)
                    null -> { //should actually never happen. But just in case, reload the whole list
                        adapterEntry.notifyDataSetChanged()
                        Log.d("adapter", "Error")
                    }
                }
            }
        }
    }
}
/*private fun showAddItemDialog(context: Context) {
  val taskEditText: EditText = EditText(context)
  taskEditText.inputType.dec()
  taskEditText.maxEms = 5
  MaterialAlertDialogBuilder(requireContext())
      .setTitle("Change Goal")
      .setView(taskEditText)
      .setPositiveButton("Change",
          DialogInterface.OnClickListener { _, _ ->
              viewModel.moneyGoalSetGoal(taskEditText.text.toString().toDouble())
          })
      .setNegativeButton("Cancel", null)
      .create()
      .show()
}*/