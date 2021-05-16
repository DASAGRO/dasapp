package kz.das.dasaccounting.core.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class SharedVM<T> : ViewModel() {

    private val resultLV = MutableLiveData<T?>()

    fun setResult(result: T?) {
        resultLV.value = result
    }

    fun getResult(): LiveData<T?> {
        return resultLV
    }

    override fun onCleared() {
        setResult(null)
        super.onCleared()
    }
}