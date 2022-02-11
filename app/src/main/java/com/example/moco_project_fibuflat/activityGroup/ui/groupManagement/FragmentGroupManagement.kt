package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.os.Bundle
import android.util.Log
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
    private lateinit var requestListNew: ArrayList<OpenRequestGroup>

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
        requestListNew = arrayListOf<OpenRequestGroup>()
        getUserData()
    }

    private fun getUserData() { //ToDo in viewModel

        database =
            FirebaseDatabase.getInstance("https://fibuflat-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Groups")

        val groupNameRef = database.child("6182134e-0bb3-4954-9eaa-cba45bc6c27c").child("openRequestsByUsers").orderByChild("OpenRequestGroup")

        val valueEventListener = object : ValueEventListener {

            //what if requests go to 0
            override fun onDataChange(snapshot: DataSnapshot) { //ToDo doesn't use recyclerViewNewArranged
                requestListNew.clear()

                if (snapshot.exists()) {
                    for (requestSnapshot in snapshot.children) {
                        val request = requestSnapshot.getValue(OpenRequestGroup::class.java)
                        requestListNew.add(request!!)
                    }
                    Log.d("adapter", requestListNew.size.toString())

                    requestRecyclerView.adapter = RecyclerViewJoinRequestAdapter(requestListNew)
                    requestList = requestListNew
                }
                else { //ToDo this part. s.a.
                    requestListNew.clear()
                    requestRecyclerView.adapter = RecyclerViewJoinRequestAdapter(requestListNew)
                    requestList.clear()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        }
        groupNameRef.addValueEventListener(valueEventListener)
    }

    private fun recyclerViewNewArranged() { //ok gets whole new list, I guess
        if (requestList.size > requestListNew.size) {
            for ((i, openRequestGroup: OpenRequestGroup) in requestList.withIndex()) {

                if (i == requestListNew.size - 1) {
                    requestRecyclerView.adapter?.notifyItemRemoved(i)
                    break
                }

                if (requestListNew[i] != requestList[i])
                    requestRecyclerView.adapter?.notifyItemRemoved(i)
            }
        }
    }

}