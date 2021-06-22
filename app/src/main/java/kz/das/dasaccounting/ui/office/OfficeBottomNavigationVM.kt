package kz.das.dasaccounting.ui.office

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.KoinComponent
import org.koin.core.inject

class OfficeBottomNavigationVM: BaseVM(), KoinComponent {

    private val userRepository: UserRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private val operationsLV = SingleLiveEvent<ArrayList<OfficeInventory>>()
    fun getOperations(): LiveData<ArrayList<OfficeInventory>> = operationsLV

    init {
        refresh()
    }

    // TODO refresh add from ui
    fun refresh() {
        retrieve()
    }

    private fun retrieve() {
        viewModelScope.launch {
            showLoading()
            try {
                val operationsArrayList: ArrayList<OfficeInventory> = arrayListOf()
                operationsArrayList.addAll(officeInventoryRepository.getOfficeMaterials())
                operationsLV.postValue(operationsArrayList)
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }

    fun getOperationsLocally() = officeInventoryRepository.getOfficeMaterialsLocally()

}