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
        //check for what happens when the internet was gone and many items were updated
        if (!checkForErrors(arrayListOld, arrayListNew))
            giveValues(0, ListCase.ERROR, arrayListNew)

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

    private fun checkForErrors(
        //ToDo if list size is the same but items changed, valueEventListener doesn't trigger.
        // For this maybe just check connection and return error. No List compare
        arrayListOld: java.util.ArrayList<A>,
        arrayListNew: java.util.ArrayList<A>,
    ): Boolean {
        var changeCounter = 0
        if (arrayListNew.size - arrayListOld.size < -1 || arrayListNew.size - arrayListOld.size > 1)
            return false
        if (arrayListOld.size > arrayListNew.size)
            for ((i, any) in arrayListOld.withIndex()) {
                if (i == arrayListNew.size - 1)
                    break
                if (any != arrayListNew[i])
                    changeCounter++
            }
        if (arrayListNew.size >= arrayListOld.size)
            for ((i, any) in arrayListNew.withIndex()) {
                if (i == arrayListOld.size - 1)
                    if (any != arrayListOld[i])
                        changeCounter++
            }

        return changeCounter <= 1
    }

    private fun added(
        arrayListOld: ArrayList<*>,
        arrayList: ArrayList<*>,
    ): Int? {
        for ((i, any: Any) in arrayList.withIndex()) {

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