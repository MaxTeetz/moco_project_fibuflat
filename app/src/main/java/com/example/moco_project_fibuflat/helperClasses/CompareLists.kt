package com.example.moco_project_fibuflat.helperClasses

import android.util.Log
import com.example.moco_project_fibuflat.data.ListCase

//compares lists and returns index of element changed and listCase
class CompareLists<A>(
    arrayListOld: ArrayList<A>,
    arrayListNew: ArrayList<A>,
    listCaseOld: ListCase?,
    giveValues: (index: Int?, listCase: ListCase?, arrayList: ArrayList<A>) -> Unit,
) {
    private var index: Int? = 0

    init {
        Log.d("compareListsIn", "$listCaseOld")
        if (listCaseOld == null) {
            Log.d("compareListsEmpty", "$listCaseOld")
            giveValues(0, ListCase.EMPTY, arrayListNew)
        }

        if(listCaseOld == ListCase.ERROR){ //for retrieving internet connection
            Log.d("compareListsError", "$listCaseOld")
            giveValues(0, ListCase.ERROR, arrayListNew)
        }

        if (arrayListOld.size > arrayListNew.size && listCaseOld != null && listCaseOld != ListCase.ERROR) { //Item deleted
            Log.d("compareListsDeleted", "$listCaseOld")
            index = deleted(arrayListOld, arrayListNew)
            giveValues(index, ListCase.DELETED, arrayListNew)
        }

        if (arrayListOld.size < arrayListNew.size && listCaseOld != null && listCaseOld != ListCase.ERROR) { //Item added
            Log.d("compareListsAdded", "$listCaseOld")
            index = added(arrayListOld, arrayListNew)
            giveValues(index, ListCase.ADDED, arrayListNew)
        }
    }

    private fun added(
        arrayListOld: ArrayList<*>,
        arrayList: ArrayList<*>,
    ): Int? {
        for ((i, any: Any) in arrayList.withIndex()) {
            Log.d("todoEntryListAddedCompareListsNewList", "$arrayList")
            Log.d("todoEntryListAddedCompareListsOldSize", "$arrayListOld")
            if (i == arrayListOld.size) { //item somewhere in between
                return i
            }
            if (arrayListOld[i] != any) { //item at lists end
                return i
            }
        }
        Log.d("compareListsAdded", "Error. Item not found")
        return null
    }

    private fun deleted(
        arrayListOld: ArrayList<*>,
        arrayList: ArrayList<*>,
    ): Int? {
        for ((i, any: Any) in arrayListOld.withIndex()) {

            if (i == arrayList.size) {
                return i
            }
            if (arrayList[i] != any) {
                return i
            }
        }

        Log.d("compareListsDeleted", "Error. Item not found")
        return null
    }
}