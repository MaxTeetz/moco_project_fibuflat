package com.example.moco_project_fibuflat.activityGroup.ui.home

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.activityGroup.adapter.MoneyPoolAdapter
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewItemDecoration
import com.example.moco_project_fibuflat.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


//val intent = Intent (requireContext(), GroupActivity::class.java)
//Log.d("homeFragment", intent.getStringExtra("user_id")!!)
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels() //ToDO back to activityViewModels() if it doesn't work

    override fun onStart() {
        super.onStart()
        (activity as GroupActivity).supportActionBar?.title = "Moneypool"

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindRecyclerView()

        viewModel.moneyGoal.observe(this.viewLifecycleOwner) { _ -> //ToDo doesnt change if popup is used, has to reload fragment
            bindGoalMoney(view)
        }

        binding.changeGoal.setOnClickListener {
            showAddItemDialog(requireContext())
        }

        binding.addEntry.setOnClickListener {
            val action = HomeFragmentDirections.actionNavHomeToAddEntryFragment()
            this.findNavController().navigate(action)
        }
    }


    private fun bindGoalMoney(view: View){ //ToDO doesn't change if entry is deleted
        (view.context.getString(
            R.string.money_amount_in_euro,
            viewModel.moneyGoal.value?.currentMoney.toString()
        ) + "/" + viewModel.moneyGoal.value?.goalMoney).also { binding.currentMoney.text = it }
        if (viewModel.getGoalReached())
            binding.currentMoney.setTextColor(resources.getColor(R.color.green))
        else
            binding.currentMoney.setTextColor(resources.getColor(R.color.red_700))
    }

    private fun bindRecyclerView() {
        val adapter = MoneyPoolAdapter {
            val action = HomeFragmentDirections.actionNavHomeToEntryDetail(it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter

        viewModel.allMoneyEntries.observe(this.viewLifecycleOwner) { entries ->
            entries.let { it ->
                adapter.submitList(it)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.addItemDecoration(
            RecyclerViewItemDecoration(
                this.requireContext(),
                R.drawable.divider_shape
            )
        )
    }

    private fun showAddItemDialog(context: Context) {
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
    }
}