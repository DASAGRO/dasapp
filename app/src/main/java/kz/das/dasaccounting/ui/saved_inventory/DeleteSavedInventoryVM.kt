package kz.das.dasaccounting.ui.saved_inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.common.InventoryType
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import java.util.*

private var RANDOM_REQUEST_ID = UUID.randomUUID().toString()

class DeleteSavedInventoryVM : BaseVM() {

    val hexadecimalCodeLiveData: LiveData<String> get() = _hexadecimalCodeLiveData
    val demicalCodeLiveData: LiveData<String> get() = _decimalCodeLiveData

    private var _hexadecimalCodeLiveData: MutableLiveData<String> = MutableLiveData()
    private var _decimalCodeLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        generateCodeForDelete()
    }

    fun getUser() = userRepository.getUser()

    private fun generateCodeForDelete() {
        val requestId = userRepository.getSavedInventory()?.let {
            when (it.type) {
                InventoryType.OFFICE -> {
                    Gson().fromJson(it.body, OfficeInventory::class.java).requestId
                        ?: RANDOM_REQUEST_ID
                }

                InventoryType.TRANSPORT -> {
                    Gson().fromJson(it.body, TransportInventory::class.java).requestId
                        ?: RANDOM_REQUEST_ID
                }

                else -> RANDOM_REQUEST_ID
            }
        } ?: run {
            RANDOM_REQUEST_ID
        }

        _hexadecimalCodeLiveData.postValue(getLastChars(requestId))
        _decimalCodeLiveData.postValue(Integer.parseInt(getLastChars(requestId), 16).toString())
    }

    private fun getLastChars(requestId: String) : String {
        return if(requestId.length <= 6){
            requestId
        } else {
            requestId.substring(requestId.length - 6)
        }
    }
}
