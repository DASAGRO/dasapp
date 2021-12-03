package kz.das.dasaccounting.ui.office.accept

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.writeObjectToLog
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.utils.InternetAccess
import org.koin.core.KoinComponent
import org.koin.core.inject

class AcceptConfirmationVM : BaseVM(), KoinComponent {
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private var officeInventory: OfficeInventory? = null

    private val fileIds: ArrayList<Int> = arrayListOf()

    private val isReportSentLV = SingleLiveEvent<Boolean>()
    fun isReportSent(): LiveData<Boolean> = isReportSentLV

    private val isFilesUploadingLV = SingleLiveEvent<Boolean>()
    fun isFilesUploading(): LiveData<Boolean> = isFilesUploadingLV

    private val isOnAwaitLV = SingleLiveEvent<Boolean>()
    fun isOnAwait(): LiveData<Boolean> = isOnAwaitLV

    private val officeInventoryAcceptedLV = SingleLiveEvent<Boolean>()
    fun isOfficeInventoryAccepted(): LiveData<Boolean> = officeInventoryAcceptedLV

    private val officeInventoryLV = SingleLiveEvent<OfficeInventory?>()
    fun getOfficeInventory(): LiveData<OfficeInventory?> = officeInventoryLV

    fun getUserRole() = userRepository.getUserRole()

    fun getUserLocation() = userRepository.getLastLocation()

    fun setOfficeInventory(officeInventory: OfficeInventory?) {
        this.officeInventory = officeInventory
        officeInventoryLV.postValue(officeInventory)
    }

    fun acceptInventory(comment: String) {
        viewModelScope.launch {
            if (InternetAccess.internetCheck(context)) {
                showLoading()
                try {
                    officeInventory?.apply {
                        latitude = getUserLocation().lat
                        longitude = getUserLocation().long
                        writeObjectToLog(this.toString(), context)

                        officeInventoryRepository.acceptInventory(this, comment, fileIds)
                        officeInventoryRepository.initCheckAwaitAcceptOperation(this)
                    }
                    officeInventoryAcceptedLV.postValue(true)
                } catch (t: Throwable) {
                    officeInventory?.let {
                        officeInventoryRepository.saveAwaitAcceptInventory(it, comment, fileIds)
                    }
                    isOnAwaitLV.postValue(true)
                } finally {
                    hideLoading()
                }
            } else {
                officeInventory?.let {
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
