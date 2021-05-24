package kz.das.dasaccounting.ui.office

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM

class OfficeBottomNavigationVM: BaseVM() {

    private val operationsLV = SingleLiveEvent<ArrayList<Any>>()
    fun getOperations(): LiveData<ArrayList<Any>> = operationsLV

    fun refresh() {

    }



}