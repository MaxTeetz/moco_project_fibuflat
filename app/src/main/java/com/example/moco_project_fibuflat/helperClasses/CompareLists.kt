package com.example.moco_project_fibuflat.helperClasses

import android.util.Log
import com.example.moco_project_fibuflat.data.ListCase

//compares lists and returns index of element changed and listCase
class CompareLists<A>(
    arrayListOld: ArrayList<A>,
    arrayListNew: ArrayList<A>,
    listCaseOld: ListCase?, //ToDo
    giveValues: (index: Int?, listCase: ListCase?, arrayList: ArrayList<A>) -> Unit,
) {
    private var index: Int? = 0

    init {

        Log.d("groupManagementViewModelListOld", "${arrayListOld.size}")
        Log.d("groupManagementViewModelListNew", "${arrayListNew.size}")
        Log.d("groupManagementViewModelListCase", "$listCaseOld")

        if (listCaseOld == null) {
            giveValues(0, ListCase.EMPTY, arrayListNew)
        }

        if (arrayListOld.size > arrayListNew.size) { //Item deleted
            index = deleted(arrayListOld, arrayListNew)
            giveValues(index, ListCase.DELETED, arrayListNew)
        }
        if ((arrayListOld.size < arrayListNew.size) && listCaseOld != null) { //Item added
            index = added(arrayListOld, arrayListNew)
            giveValues(index, ListCase.ADDED, arrayListNew)
        }
    }

    private fun added( //maybe
        arrayListOld: ArrayList<*>,
        arrayList: ArrayList<*>,
    ): Int {
        for ((i, any: Any) in arrayList.withIndex()) {

            if (i == arrayListOld.size) { //item somewhere in between
                Log.d("groupManagementViewModelIndex", "$index")
                return i
            }
            if (arrayListOld[i] != any) { //item at lists end
                Log.d("groupManagementViewModelIndex", "$index")
                return i
            }
        }
        Log.d("groupManagementViewModelIndex", "$index")
        return 0
    }

    private fun deleted(
        arrayListOld: ArrayList<*>,
        arrayList: ArrayList<*>,
    ): Int {
        for ((i, any: Any) in arrayListOld.withIndex()) {

            if (i == arrayList.size) {
                Log.d("groupManagementViewModelIndex", "$index")
                return i
            }
            if (arrayList[i] != any) {
                Log.d("groupManagementViewModelIndex", "$index")
                return i
            }
        }
        Log.d("groupManagementViewModelIndex", "$index")
        return 0
    }
}