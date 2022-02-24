package com.example.moco_project_fibuflat.activityGroup.ui.home

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activityGroup.adapter.MoneyPoolAdapter
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewItemDecoration
import com.example.moco_project_fibuflat.data.ListCase
import com.example.moco_project_fibuflat.databinding.FragmentHomeBinding
import com.example.moco_project_fibuflat.helperClasses.OftenNeededData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var neededData: OftenNeededData
    private lateinit var adapterEntry: MoneyPoolAdapter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val coroutine1 = Job()
    private val coroutineScope1 = CoroutineScope(coroutine1 + Dispatchers.Main)
    private val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)

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
            requireActivity().registerReceiver(connectivityReceiver, intentFilter)
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
                when (viewModel.listCase) {
                    ListCase.EMPTY -> adapterEntry.submitList(it)
                    ListCase.DELETED -> adapterEntry.notifyItemRemoved(viewModel.index!!)
                    ListCase.ADDED -> adapterEntry.notifyItemInserted(viewModel.index!!)
                    ListCase.ERROR -> {
                        adapterEntry.notifyDataSetChanged()
                    }
                    else -> { //if the internet goes of and many items changed, deleted or got added
                        adapterEntry.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeListeners()
        requireActivity().unregisterReceiver(connectivityReceiver)
    }

    private val connectivityReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
                val noConnectivity: Boolean =
                    intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                if (noConnectivity) {
                    Log.d("HomeFragment", "noConnectivity")
                    Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show()
                    viewModel.removeListeners()
                } else {
                    Log.d("HomeFragment", "connectivity")
                    Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}