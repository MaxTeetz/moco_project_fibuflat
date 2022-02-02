package com.example.moco_project_fibuflat.ActivityGroup.ui.home.AddEntry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.databinding.EntryDetailBinding


class EntryDetail : Fragment() {

    private var _binding: EntryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.entry_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.message.text = ""
    }
}