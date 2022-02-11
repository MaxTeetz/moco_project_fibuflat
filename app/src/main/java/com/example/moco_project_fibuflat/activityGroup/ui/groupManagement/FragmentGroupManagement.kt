package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moco_project_fibuflat.activityGroup.GroupActivity
import com.example.moco_project_fibuflat.activityGroup.adapter.RecyclerViewJoinRequestAdapter
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.databinding.FragmentGroupManagementBinding
import com.google.firebase.database.*

class FragmentGroupManagement : Fragment() {

    private var _binding: FragmentGroupManagementBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GroupManagementViewModel by viewModels()

    private lateinit var database: DatabaseReference
    private lateinit var requestRecyclerView: RecyclerView
    private lateinit var requestList: ArrayList<OpenRequestGroup>

    private lateinit var groupID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as GroupActivity).supportActionBar?.title = "Group Management"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGroupManagementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestRecyclerView = binding.recyclerViewJoinRequests
        requestRecyclerView.layoutManager = LinearLayoutManager(this.context)
        requestRecyclerView.setHasFixedSize(true)

        requestList = arrayListOf<OpenRequestGroup>()
        getUserData()
    }

    private fun getUserData() { //ToDo in viewModel

        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Groups")
        val groupNameRef = database.child("98ace245-f51c-4644-ad21-9f23c4f56af2").child("openRequests").orderByChild("OpenRequestGroup")
        val valueEventListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (requestSnapshot in snapshot.children) {
                        val request = requestSnapshot.getValue(OpenRequestGroup::class.java)
                        requestList.add(request!!)
                    }

                    requestRecyclerView.adapter = RecyclerViewJoinRequestAdapter(requestList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //ToDo ask prof
            }

        }
        groupNameRef.addListenerForSingleValueEvent(valueEventListener)
    }
}