package com.example.moco_project_fibuflat.activityGroup.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.activityGroup.adapter.MoneyPoolAdapter
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewItemDecoration
import com.example.moco_project_fibuflat.activityGroup.data.MoneyPoolEntry
import com.example.moco_project_fibuflat.databinding.FragmentHomeBinding
import java.util.*
import kotlin.random.Random

//val intent = Intent (requireContext(), GroupActivity::class.java)
//Log.d("homeFragment", intent.getStringExtra("user_id")!!)
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Log.d("homeFragment", "onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Log.d("homeFragment", "onViewCreated")
        val adapter = MoneyPoolAdapter {
            val action = HomeFragmentDirections.actionNavHomeToEntryDetail(it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.adapter = adapter

        viewModel.allMoneyEntries.observe(this.viewLifecycleOwner) { entries ->
            entries.let {
                adapter.submitList(it)
                binding.currentMoney.text = view.context.getString(
                    R.string.money_amount_in_euro,
                    viewModel.getCurrentMoney().toString()
                )
            }
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.addItemDecoration(RecyclerViewItemDecoration(this.requireContext(), R.drawable.divider_shape))
        binding.addEntry.setOnClickListener {

            viewModel.addItem(
                MoneyPoolEntry(
                    UUID.randomUUID(),
                    "Max Mustermann",
                    Random.nextInt(0, 9999),
                    "03.02.2022",
                    "This is a message"
                )
            )

            adapter.notifyItemInserted(viewModel.allMoneyEntries.value!!.size-1) //ToDo also with delete etc.
            //val action = HomeFragmentDirections.actionNavHomeToAddEntryFragment()
            //this.findNavController().navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("homeFragment", "onStart")
        (activity as GroupActivity).supportActionBar?.title = "Moneypool"

    }

    override fun onResume() {
        super.onResume()
        Log.d("homeFragment", "onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.d("homeFragment", "onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.d("homeFragment", "onStop")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("homeFragment", "onDestroyView")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("homeFragment", "onDestroy")

    }

}