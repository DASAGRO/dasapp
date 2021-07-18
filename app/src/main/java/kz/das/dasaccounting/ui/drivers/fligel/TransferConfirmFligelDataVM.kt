package kz.das.dasaccounting.ui.drivers.fligel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.DriverInventoryRepository
import kz.das.dasaccounting.domain.OfficeInventoryRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import org.koin.core.inject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class TransferConfirmFligelDataVM: BaseVM() {

    private val driverInventoryRepository: DriverInventoryRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private val userRepository: UserRepository by inject()

    private var fligelProduct: FligelProduct? = null

    private val fileIds: ArrayList<Int> = arrayListOf()

    private val isFilesUploadingLV = SingleLiveEvent<Boolean>()
    fun isFilesUploading(): LiveData<Boolean> = isFilesUploadingLV

    private val fligelDataLV = SingleLiveEvent<FligelProduct?>()
    fun getFligelData(): LiveData<FligelProduct?> = fligelDataLV

    private val isOnAwaitLV = SingleLiveEvent<Boolean>()
    fun isOnAwait(): LiveData<Boolean> = isOnAwaitLV

    fun setOfficeInventory(officeInventory: FligelProduct?) {
        this.fligelProduct = officeInventory
        fligelDataLV.postValue(officeInventory)
    }

    private val driverInventoryDataLV = SingleLiveEvent<Boolean>()
    fun isTransportDataAccepted(): LiveData<Boolean> = driverInventoryDataLV

    fun getUserRole() = userRepository.getUserRole()

    fun acceptInventory(comment: String) {
        viewModelScope.launch {
            showLoading()
            try {
                fligelProduct?.let {
                    it.comment = comment
                    driverInventoryRepository.receiveFligelData(it, fileIds)
                }
                driverInventoryRepository.getDriverTransports()
                officeInventoryRepository.getOfficeMaterials()
                driverInventoryDataLV.postValue(true)
            } catch (t: Throwable) {
                if (t is SocketTimeoutException
                    || t is UnknownHostException
                    || t is ConnectException
                ) {
                    fligelProduct?.let {
                        driverInventoryRepository.saveAwaitReceiveFligelData(it)
                    }
                    isOnAwaitLV.postValue(true)
                } else {
                    throwableHandler.handle(t)
                    driverInventoryDataLV.postValue(false)
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