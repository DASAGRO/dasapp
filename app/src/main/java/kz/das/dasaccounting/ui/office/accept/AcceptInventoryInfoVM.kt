package kz.das.dasaccounting.ui.office.accept

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.data.office.OfficeInventory

class AcceptInventoryInfoVM: BaseVM() {

    private val officeInventoryLV = SingleLiveEvent<OfficeInventory?>()
    fun getOfficeInventory(): LiveData<OfficeInventory?> = officeInventoryLV

    fun setOfficeInventory(officeInventory: OfficeInventory?) = officeInventoryLV.postValue(officeInventory)



}