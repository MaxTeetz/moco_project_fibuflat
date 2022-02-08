package com.example.moco_project_fibuflat.activitySelectGroup.ui.createGroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moco_project_fibuflat.R
import com.example.moco_project_fibuflat.activitySelectGroup.SelectGroupActivity

class CreateGroupFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as SelectGroupActivity).supportActionBar?.title = "Create Group"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_group, container, false)
    }
}