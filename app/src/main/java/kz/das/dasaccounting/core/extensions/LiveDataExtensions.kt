package kz.das.dasaccounting.core.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData


fun zipLiveDataAny(vararg liveItems: LiveData<*>): LiveData<ArrayList<Any>> {
    //MediatorLiveData used to merge multiple LiveDatas
    return MediatorLiveData<ArrayList<Any>>().apply {
        val zippedObjects = ArrayList<Any>()
        liveItems.forEach {
            //Add each LiveData as a source for the MediatorLiveData
            addSource(it) { item ->
                //Add value to list
                item?.let { it1 -> zippedObjects.add(it1) }
                if (zippedObjects.size == liveItems.size) {
                    //If all the LiveData items has returned a value, save that value in MediatorLiveData.
                    value = zippedObjects
                    //Clear the list for next time
                    zippedObjects.clear()
                }
            }
        }
    }
}

inline fun <reified T>zipLiveDataGenerics(vararg liveItems: LiveData<*>): LiveData<ArrayList<T>> {
    return MediatorLiveData<ArrayList<T>>().apply {
        val zippedObjects = ArrayList<T>()
        liveItems.forEach {
            //Add each LiveData as a source for the MediatorLiveData
            addSource(it) { item ->
                //Add value to list
                item?.let { tempItem ->
                    if (tempItem is T) {
                        zippedObjects.add(tempItem)
                    }
                }
                if (zippedObjects.size == liveItems.size) {
                    //If all the LiveData items has returned a value, save that value in MediatorLiveData.
                    value = zippedObjects
                    //Clear the list for next time
                    zippedObjects.clear()
                }
            }
        }
    }
}



fun zipLiveDataNullable(vararg liveItems: LiveData<*>): LiveData<ArrayList<Any?>> {
    return MediatorLiveData<ArrayList<Any?>>().apply {
        val zippedObjects = ArrayList<Any?>()
        liveItems.forEach {
            addSource(it) { item ->
                zippedObjects.add(item)
                if (zippedObjects.size == liveItems.size) {
                    value = zippedObjects
                    zippedObjects.clear()
                }
            }
        }
    }
}