package kz.das.dasaccounting.ui.office.accept

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.writeObjectToLog
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.inject

class AcceptInventoryInfoVM: BaseVM() {

    private val context: Context by inject()

    private val userRepository: UserRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private var officeInventory: OfficeInventory? = null

    private val officeInventoryLV = SingleLiveEvent<OfficeInventory?>()
    fun getOfficeInventory(): LiveData<OfficeInventory?> = officeInventoryLV

    private val isOnAwaitLV = SingleLiveEvent<Boolean>()
    fun isOnAwait(): LiveData<Boolean> = isOnAwaitLV

    private val officeInventoryAcceptedLV = SingleLiveEvent<Boolean>()
    fun isOfficeInventoryAccepted(): LiveData<Boolean> = officeInventoryAcceptedLV

    fun setOfficeInventory(officeInventory: OfficeInventory?) {
        this.officeInventory = officeInventory
        officeInventoryLV.postValue(officeInventory)
    }

    fun getLocation() = userRepository.getLastLocation()

    fun acceptInventory(comment: String) {
        viewModelScope.launch {
            showLoading()
            try {
                officeInventory?.apply {
                    latitude = getLocation().lat
                    longitude = getLocation().long
                    writeObjectToLog(this.toString(), context)

                    officeInventoryRepository.acceptInventory(this, comment, arrayListOf())
                    officeInventoryRepository.initCheckAwaitAcceptOperation(this)
                }
                officeInventoryAcceptedLV.postValue(true)
            } catch (t: Throwable) {
                officeInventory?.let {
                    officeInventoryRepository.saveAwaitAcceptInventory(it, comment, arrayListOf())
                }
                isOnAwaitLV.postValue(true)
            } finally {
                hideLoading()
            }
        }
    }
}