package com.example.moco_project_fibuflat.helperClasses

import android.util.Log
import com.example.moco_project_fibuflat.data.ListCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//returns a firebase realtime database snapshot
class GetSnapshot<A>(
    private var entryList: ArrayList<A>,
    private var entryListOld: ArrayList<A>,
    private var listCaseOld: ListCase?,
    private var dataType: A,
    private val giveValues: (index: Int?, listCase: ListCase?, entryList: ArrayList<A>) -> Unit,
) : ValueEventListener {
    private var index: Int? = 0

    override fun onDataChange(snapshot: DataSnapshot) {
        Log.d("groupManagementViewModelLisCaseInGetSnapshot0", "$listCaseOld")
        if (entryList.isNotEmpty())
            entryList.clear()

        if (snapshot.exists()) {
            for (entrySnapshot in snapshot.children) {
                val entry =
                    entrySnapshot?.getValue(dataType!!::class.java)
                entryList.add(entry!!)
            }
        }
        CompareLists(entryListOld, entryList, listCaseOld)
        { index, listCase, arrayList -> setData(index, listCase, arrayList) }
        setEntryListOld()
        giveValues(index, listCaseOld, entryList)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("adapter", "cancelled")
    }

    private fun setData(index: Int?, listCase: ListCase?, arrayList: ArrayList<A>) {
        Log.d("groupManagementViewModelLisCaseInGetSnapshot1", "$listCase")
        this.index = index
        this.listCaseOld = listCase
        this.entryList = arrayList
    }

    private fun setEntryListOld() {
        entryListOld.clear()
        for (mpe in entryList) {
            entryListOld.add(mpe)
        }
    }
}