package com.example.moco_project_fibuflat.ActivityGroup.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.MoneyPoolAdapter
import com.example.moco_project_fibuflat.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MoneyPoolAdapter {
            val action = HomeFragmentDirections.actionNavHomeToMoneyPoolEntryDetailFragment()
            this.findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter

        viewModel.allMoneyEntries.observe(this.viewLifecycleOwner){entries -> entries.let { adapter.submitList(it) }}
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.addEntry.setOnClickListener{

            //val i: Int = 5
            //val moneyPoolEntry = MoneyPoolEntry(this,i)
            //viewModel.addEntry(moneyPoolEntry)
            val action = HomeFragmentDirections.actionNavHomeToAddEntryFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}