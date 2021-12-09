package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.extensions.zipLiveDataAny
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.common.InventoryType
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.drivers.toHistoryTransfer
import kz.das.dasaccounting.domain.data.drivers.toSent
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.office.toHistory
import org.koin.core.inject

class HistoryGivenVM: BaseVM() {

    private val driverInventoryRepository: DriverInventoryRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    fun getUser() = userRepository.getUser()

    fun getZippedHistory() = zipLiveDataAny(getHistoryWarehouseInventoriesLocally(),
        getHistoryTransportInventoriesLocally(),
        getHistoryOfficeInventoriesLocally(),
        sentTransportInventoryLocally(),
        sentOfficeInventoryLocally()
    )

    fun getSavedInventoryHistory(): HistoryTransfer? {
        userRepository.getSavedInventory()?.let {
            when (it.type) {
                InventoryType.OFFICE -> {
                    return Gson().fromJson(it.body, OfficeInventory::class.java).toHistory()
                }

                InventoryType.TRANSPORT -> {
                    return Gson().fromJson(it.body, TransportInventory::class.java).toSent().toHistoryTransfer()
                }

                InventoryType.FLIGER -> {
//                    return Gson().fromJson(it.body, FligelProduct::class.java).to
                    return null
                }

                InventoryType.WAREHOUSE -> {
                    return null
                }
            }
        } ?: run { return null }
    }

    private fun getHistoryWarehouseInventoriesLocally() = userRepository.getHistorySentWarehouseInventoriesLocally()

    private fun getHistoryTransportInventoriesLocally() = userRepository.getHistorySentTransportInventoriesLocally()

    private fun getHistoryOfficeInventoriesLocally() = userRepository.getHistorySentOfficeInventoriesLocally()

    private fun sentTransportInventoryLocally() = driverInventoryRepository.getDriverSentMaterialsLocally()

    private fun sentOfficeInventoryLocally() = officeInventoryRepository.getHistoryOfficeSentMaterialsLocally()

}