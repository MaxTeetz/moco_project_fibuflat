package com.example.moco_project_fibuflat.helperClasses

import android.util.Log
import com.example.moco_project_fibuflat.data.ListCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

//returns a firebase realtime database snapshot
class GetSnapshot<A>(
    entryList: ArrayList<A>,
    entryListOld: ArrayList<A>,
    listCaseOld: ListCase?,
    databaseReference: DatabaseReference,
    giveValues: (index: Int?, listCase: ListCase?, entryList: ArrayList<A>, entryListOld: ArrayList<A>) -> Unit,
) {
    private var valueEventListener: ValueEventListener
    private var index: Int? = 0
    private var listCase: ListCase? = null
    private var entryList: ArrayList<A> = arrayListOf()
    private var entryListOld: ArrayList<A> = arrayListOf()
    init {
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (entryList.isNotEmpty())
                    entryList.clear()
                if (snapshot.exists()) {
                    for (entrySnapshot in snapshot.children) {
                        val entry = entrySnapshot.getValue(Any::class.java)
                        Log.d("getSnapShot", "$entry")
                        entryList.add(entry!! as A)
                    }
                    CompareLists(entryListOld, entryList, listCaseOld)
                    {index, listCase, arrayList -> setData(index, listCase, arrayList) }
                    setEntryListOld()

                    giveValues(index,listCase,entryList,entryListOld)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("adapter", "cancelled")
            }
        }
        databaseReference.addValueEventListener(valueEventListener)
    }

    private fun setData(index: Int?, listCase: ListCase?, arrayList: ArrayList<A>){
        this.index = index
        this.listCase = listCase
        this.entryList = arrayList
    }

    private fun setEntryListOld() {
        entryListOld.clear()
        for (mpe in entryList) {
            entryListOld.add(mpe)
        }
    }
}