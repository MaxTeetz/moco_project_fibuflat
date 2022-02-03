package com.example.moco_project_fibuflat.ActivityGroup.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.Data.MoneyPoolEntry
import com.example.moco_project_fibuflat.ActivityGroup.Adapter.MoneyPoolAdapter
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.databinding.FragmentHomeBinding
import java.util.*

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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.addEntry.setOnClickListener {

            viewModel.addEntry(
                MoneyPoolEntry(
                    UUID.randomUUID(),
                    "Max Mustermann",
                    9999,
                    "03.02.2022",
                    "This is a message"
                )
            )
            //val action = HomeFragmentDirections.actionNavHomeToAddEntryFragment()
            //this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}