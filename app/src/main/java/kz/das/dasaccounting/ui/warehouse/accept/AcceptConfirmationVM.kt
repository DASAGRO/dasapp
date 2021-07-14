package kz.das.dasaccounting.ui.warehouse.accept

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.WarehouseInventoryRepository
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.core.KoinComponent
import org.koin.core.inject

class AcceptConfirmationVM : BaseVM(), KoinComponent {

    private val warehouseRepository: WarehouseInventoryRepository by inject()
    private val userRepository: UserRepository by inject()

    private var warehouseInventory: WarehouseInventory? = null

    private val fileIds: ArrayList<Int> = arrayListOf()

    private val isReportSentLV = SingleLiveEvent<Boolean>()
    fun isReportSent(): LiveData<Boolean> = isReportSentLV

    private val isFilesUploadingLV = SingleLiveEvent<Boolean>()
    fun isFilesUploading(): LiveData<Boolean> = isFilesUploadingLV

    private val warehouseInventoryLV = SingleLiveEvent<WarehouseInventory?>()
    fun getWarehouseInventory(): LiveData<WarehouseInventory?> = warehouseInventoryLV

    fun setWarehouseInventory(warehouse: WarehouseInventory?) {
        this.warehouseInventory = warehouse
        warehouseInventoryLV.postValue(warehouse)
    }

    private val warehouseInventoryAcceptedLV = SingleLiveEvent<Boolean>()
    fun isWarehouseInventoryAccepted(): LiveData<Boolean> = warehouseInventoryAcceptedLV

    fun acceptInventory(comment: String) {
        viewModelScope.launch {
            showLoading()
            try {
                warehouseInventory?.let {
                    warehouseRepository.acceptInventory(it, comment, fileIds)
                }
                warehouseInventoryAcceptedLV.postValue(true)
            } catch (t: Throwable) {
                throwableHandler.handle(t)
                warehouseInventoryAcceptedLV.postValue(false)
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
