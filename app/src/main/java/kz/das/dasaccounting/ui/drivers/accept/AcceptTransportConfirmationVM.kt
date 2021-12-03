package kz.das.dasaccounting.ui.drivers.accept

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.writeObjectToLog
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.utils.InternetAccess
import org.koin.core.KoinComponent
import org.koin.core.inject

class AcceptTransportConfirmationVM : BaseVM(), KoinComponent {

    private val officeInventoryRepository: DriverInventoryRepository by inject()

    private var transportInventory: TransportInventory? = null

    private val fileIds: ArrayList<Int> = arrayListOf()

    private val isReportSentLV = SingleLiveEvent<Boolean>()
    fun isReportSent(): LiveData<Boolean> = isReportSentLV

    private val isFilesUploadingLV = SingleLiveEvent<Boolean>()
    fun isFilesUploading(): LiveData<Boolean> = isFilesUploadingLV

    private val isOnAwaitLV = SingleLiveEvent<Boolean>()
    fun isOnAwait(): LiveData<Boolean> = isOnAwaitLV

    private val transportInventoryLV = SingleLiveEvent<TransportInventory?>()
    fun getTransportInventory(): LiveData<TransportInventory?> = transportInventoryLV

    fun getUserRole() = userRepository.getUserRole()

    fun getUserLocation() = userRepository.getLastLocation()

    fun setTransportInventory(officeInventory: TransportInventory?) {
        this.transportInventory = officeInventory
        transportInventoryLV.postValue(officeInventory)
    }

    private val driverInventoryAcceptedLV = SingleLiveEvent<Boolean>()
    fun isTransportInventoryAccepted(): LiveData<Boolean> = driverInventoryAcceptedLV

    fun acceptInventory(comment: String) {
        viewModelScope.launch {
            if (InternetAccess.internetCheck(context)) {
                showLoading()
                try {
                    transportInventory?.apply {
                        latitude = getUserLocation().lat
                        longitude = getUserLocation().long
                        writeObjectToLog(this.toString(), context)

                        officeInventoryRepository.acceptInventory(this, comment, fileIds)
                        officeInventoryRepository.addItem(this)
                    }
                    driverInventoryAcceptedLV.postValue(true)
                } catch (t: Throwable) {
                    transportInventory?.let {
                        officeInventoryRepository.saveAwaitAcceptInventory(it, comment, fileIds)
                    }
                    isOnAwaitLV.postValue(true)
                } finally {
                    hideLoading()
                }
            } else {
                transportInventory?.let {
                    officeInventoryRepository.saveAwaitAcceptInventory(it, comment, fileIds)
                }
                isOnAwaitLV.postValue(true)
            }
        }
    }

    fun remove(position: Int) {
        if (!fileIds.isNullOrEmpty() && position > fileIds.size) {
            fileIds.removeAt(position)
        }
    }

    fun upload(list: List<Uri>) {
        viewModelScope.launch {
            isFilesUploadingLV.postValue(true)
            try {
                for (uri in list) {
                    withContext(Dispatchers.IO) {
                        fileIds.add(userRepository.uploadFile(uri).id ?: 0)
                    }
                }
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                isFilesUploadingLV.postValue(false)
            }
        }
    }

}
