package com.example.moco_project_fibuflat.activityGroup.ui.groupManagement

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moco_project_fibuflat.data.OpenRequestGroup
import com.example.moco_project_fibuflat.data.repository.OftenNeededData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class GroupManagementViewModel : ViewModel() {
    private lateinit var requestList: ArrayList<OpenRequestGroup>
    private lateinit var requestListOld: ArrayList<OpenRequestGroup>
    private lateinit var databaseGroup: DatabaseReference
    private lateinit var databaseUser: DatabaseReference

    private var _requestListNew: MutableLiveData<ArrayList<OpenRequestGroup>> = MutableLiveData()
    val requestListNew: LiveData<ArrayList<OpenRequestGroup>> get() = _requestListNew

    /*private var _listCases: ListCases? = ListCases.EMPTY
    val listCases get() = _listCases

    private var _index: Int? = null
    val index get() = _index*/ //ToDo code for part below. See ToDo

    fun getUserData(database: DatabaseReference) {
        this.databaseGroup = database //ToDo ask Prof -> better in viewModel and given to function or define in here

        val groupNameRef =
            database.child("6182134e-0bb3-4954-9eaa-cba45bc6c27c").child("openRequestsByUsers")
                .orderByChild("OpenRequestGroup")

        requestList = arrayListOf()
        requestListOld = arrayListOf()

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (requestList.isNotEmpty())
                    requestList.clear()

                if (snapshot.exists()) {
                    for (requestSnapshot in snapshot.children) {
                        val request = requestSnapshot.getValue(OpenRequestGroup::class.java)
                        requestList.add(request!!)
                    }
                }
                _requestListNew.value = requestList
                //recyclerViewNewArranged() ToDo doesn't work because oldList is always same as current list
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("adapter", "cancelled")
            }
        }
        groupNameRef.addValueEventListener(valueEventListener)
    }

    /*private fun recyclerViewNewArranged() { //ok gets whole new list, I guess

        Log.d("adapter2", requestListOld.size.toString())
        Log.d("adapter2", requestList.size.toString())
        if (requestListOld.isEmpty()) {
            Log.d("adapter", "empty")
            _listCases = ListCases.EMPTY
        }

        if (requestListOld.size > requestList.size) { //Item deleted
            Log.d("adapter", "deleted")

            _listCases = ListCases.DELETED

            for ((i, openRequestGroup: OpenRequestGroup) in requestListOld.withIndex()) { //item somewhere before the lists end
                if (requestList[i] != openRequestGroup) {
                    _index = i
                }
                if (i == requestList.size - 2) { //item at lists end
                    _index = i + 1
                }
            }
        }
        if (requestListOld.size < requestList.size && requestListOld.isNotEmpty()) { //Item added
            Log.d("adapter", "added")
            _listCases = ListCases.ADDED
        }
        requestListOld = requestList
        _requestListNew.value = requestList
    }*/

    fun acceptUser(){
        val oftenNeededData:OftenNeededData = OftenNeededData()
        databaseUser = oftenNeededData.dataBaseUsers
        Log.d("adapter", "databaseUser.toString()")
    }

    fun declineUser(){

    }
}