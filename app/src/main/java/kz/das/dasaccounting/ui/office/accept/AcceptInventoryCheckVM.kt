package kz.das.dasaccounting.ui.office.accept

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.data.office.OfficeInventory

class AcceptInventoryCheckVM: BaseVM() {

    private var officeInventory: OfficeInventory? = null

    private val officeInventoryLV = SingleLiveEvent<OfficeInventory?>()
    fun getOfficeInventory(): LiveData<OfficeInventory?> = officeInventoryLV

    fun getLocalInventory() = officeInventory

    fun setLocalInventory(officeInventory: OfficeInventory) {
        this.officeInventory = officeInventory
    }

    fun getUserRole() = userRepository.getUserRole()

    fun setOfficeInventory(officeInventory: OfficeInventory?) {
        this.officeInventory = officeInventory
        this.officeInventory?.receiverUUID = userRepository.getUser()?.userId
        this.officeInventory?.receiverName = userRepository.getUser()?.lastName + " " +
                if (userRepository.getUser()?.firstName?.length ?: 0 > 0) {
                    userRepository.getUser()?.firstName?.toCharArray()?.let { it[0] }?.plus(".")
                } else {
                    " "
                }  +

                if (userRepository.getUser()?.middleName?.length ?: 0 > 0) {
                    userRepository.getUser()?.middleName?.toCharArray()?.let { it[0] }?.plus(".")
                } else {
                    " "
                }
        officeInventoryLV.postValue(officeInventory)
    }

}