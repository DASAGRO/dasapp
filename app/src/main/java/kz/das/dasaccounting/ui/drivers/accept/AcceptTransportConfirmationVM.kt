package kz.das.dasaccounting.ui.drivers.accept

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class AcceptTransportConfirmationVM : BaseVM(), KoinComponent {

    private val officeInventoryRepository: DriverInventoryRepository by inject()
    private val userRepository: UserRepository by inject()

    private var transportInventory: TransportInventory? = null

    private val fileIds: ArrayList<Int> = arrayListOf()

    private val isReportSentLV = SingleLiveEvent<Boolean>()
    fun isReportSent(): LiveData<Boolean> = isReportSentLV

    private val isFilesUploadingLV = SingleLiveEvent<Boolean>()
    fun isFilesUploading(): LiveData<Boolean> = isFilesUploadingLV


    private val transportInventoryLV = SingleLiveEvent<TransportInventory?>()
    fun getOfficeInventory(): LiveData<TransportInventory?> = transportInventoryLV

    fun setOfficeInventory(officeInventory: TransportInventory?) {
        this.transportInventory = officeInventory
        transportInventoryLV.postValue(officeInventory)
    }

    private val driverInventoryAcceptedLV = SingleLiveEvent<Boolean>()
    fun isTransportInventoryAccepted(): LiveData<Boolean> = driverInventoryAcceptedLV

    fun acceptInventory(comment: String) {
        viewModelScope.launch {
            showLoading()
            try {
                transportInventory?.let {
                    officeInventoryRepository.acceptInventory(it, comment, fileIds)
                }
                officeInventoryRepository.getDriverTransports()
                driverInventoryAcceptedLV.postValue(true)
            } catch (t: Throwable) {
                if (t is SocketTimeoutException
                    || t is UnknownHostException
                    || t is ConnectException
                ) {
                    transportInventory?.let {
                        officeInventoryRepository.saveAwaitAcceptInventory(it, comment, fileIds)
                    }
                    driverInventoryAcceptedLV.postValue(true)
                } else {
                    throwableHandler.handle(t)
                    driverInventoryAcceptedLV.postValue(false)
                }
            } finally {
                hideLoading()
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
