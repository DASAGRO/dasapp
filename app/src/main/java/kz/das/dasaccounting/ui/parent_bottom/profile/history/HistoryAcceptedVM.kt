package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.extensions.zipLiveDataAny
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.history.QrDateFull
import kz.das.dasaccounting.domain.data.history.QrDateShort
import org.koin.core.inject

class HistoryAcceptedVM: BaseVM() {

    private val userRepository: UserRepository by inject()
    private val driverInventoryRepository: DriverInventoryRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    init {
        retrieve()
    }

    private fun retrieve() {
        viewModelScope.launch {
            try {
                userRepository.getHistoryOfficeInventories()
                userRepository.getHistoryTransportInventories()
                userRepository.getHistoryWarehouseInventories()
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            }
        }
    }

    fun getUser() = userRepository.getUser()

    fun getZippedHistory() = zipLiveDataAny(getHistoryWarehouseInventoriesLocally(),
        getHistoryTransportInventoriesLocally(),
        getHistoryOfficeInventoriesLocally(),
        acceptedTransportInventoryLocally(),
        acceptedOfficeInventoryLocally())

    fun getQrData(qrString: String?, type: String?, status: String?): String? {
        val gson = Gson()
        val qrDataFull = gson.fromJson(qrString, QrDateFull::class.java) ?: return null
        val qrDateShort = QrDateShort(
                receiverName = userRepository.getUser()?.lastName + " " +
                        if (userRepository.getUser()?.firstName?.length ?: 0 > 0) {
                            userRepository.getUser()?.firstName?.toCharArray()?.let { it[0] }?.plus(".")
                        } else {
                            " "
                        } +

                        if (userRepository.getUser()?.middleName?.length ?: 0 > 0) {
                            userRepository.getUser()?.middleName?.toCharArray()?.let { it[0] }?.plus(".")
                        } else {
                            " "
                        },
                receiverUUID = userRepository.getUser()?.userId,
                transferType = type,
                requestId = qrDataFull.requestId ?: ""
        )
         return gson.toJson(qrDateShort)
//        return if(status == "В ожидании") {
//            gson.toJson(qrDateShort)
//        } else {
//            gson.toJson(qrDataFull)
//        }
    }

    private fun getHistoryWarehouseInventoriesLocally() = userRepository.getHistoryAcceptedWarehouseInventoriesLocally()

    private fun getHistoryTransportInventoriesLocally() = userRepository.getHistoryAcceptedTransportInventoriesLocally()

    private fun getHistoryOfficeInventoriesLocally() = userRepository.getHistoryAcceptedOfficeInventoriesLocally()

    private fun acceptedTransportInventoryLocally() = driverInventoryRepository.getDriverAcceptedMaterialsLocally()

    private fun acceptedOfficeInventoryLocally() = officeInventoryRepository.getHistoryOfficeAcceptedMaterialsLocally()

}