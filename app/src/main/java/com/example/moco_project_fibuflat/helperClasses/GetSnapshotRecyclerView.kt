package com.example.moco_project_fibuflat.helperClasses

import android.util.Log
import com.example.moco_project_fibuflat.data.ListCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

//returns a firebase realtime database snapshot
class GetSnapshotRecyclerView<A>(
    private var entryList: ArrayList<A>,
    private var entryListOld: ArrayList<A>,
    private var listCaseOld: ListCase?,
    private var dataType: A,
    private val giveValues: (index: Int?, listCase: ListCase?, entryList: ArrayList<A>, entryListOld: ArrayList<A>) -> Unit,
) : ValueEventListener {
    private var index: Int? = 0

    override fun onDataChange(snapshot: DataSnapshot) {
        if (entryList.isNotEmpty())
            entryList.clear()

        Log.d("compareListsBeforeCall", "$listCaseOld")
        if (snapshot.exists()) {
            for (entrySnapshot in snapshot.children) {
                val entry =
                    entrySnapshot?.getValue(dataType!!::class.java)
                entryList.add(entry!!)
            }
        }
        Log.d("compareListsBeforeCall", "$listCaseOld")
        CompareLists(entryListOld, entryList, listCaseOld)
        { index, listCase, arrayList -> setData(index, listCase, arrayList) }
        giveValues(index, listCaseOld, entryList, entryListOld)
    }

    override fun onCancelled(error: DatabaseError) {
        Log.d("adapter", "$error")
    }

    private fun setData(index: Int?, listCase: ListCase?, arrayList: ArrayList<A>) { //doesn't work if the data is directly returned from compareList; so needs a helper function
        this.index = index
        this.listCaseOld = listCase
        this.entryList = arrayList
        setEntryListOld(entryList)
    }

    private fun setEntryListOld(arrayList: ArrayList<A>){
        entryListOld.clear()
        for(entry in arrayList)
            entryListOld.add(entry)
    }
}