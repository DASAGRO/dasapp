package kz.das.dasaccounting.ui.office

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.inject

class OfficeBottomNavigationVM: BaseVM() {

    private val userRepository: UserRepository by inject()

    private val operationsLV = SingleLiveEvent<ArrayList<Any>>()
    fun getOperations(): LiveData<ArrayList<Any>> = operationsLV

    fun refresh() {

    }

}