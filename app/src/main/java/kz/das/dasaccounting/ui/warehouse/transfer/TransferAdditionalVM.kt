package kz.das.dasaccounting.ui.warehouse.transfer


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import org.koin.core.inject

class TransferAdditionalVM: BaseVM() {

    private val userRepository: UserRepository by inject()
    private var warehouseInventory: WarehouseInventory? = null
    private val fileIds: ArrayList<Int> = arrayListOf()

    fun setWarehouseInventory(warehouseInventory: WarehouseInventory?) {
        this.warehouseInventory = warehouseInventory
    }

    fun getWarehouseInventory(): WarehouseInventory? {
        return warehouseInventory
    }

    fun getFileIds(): ArrayList<Int> = fileIds

    fun remove(position: Int) {
        if (!fileIds.isNullOrEmpty() && position > fileIds.size) {
            fileIds.removeAt(position)
        }
    }

    private val isFilesUploadingLV = SingleLiveEvent<Boolean>()
    fun isFilesUploading(): LiveData<Boolean> = isFilesUploadingLV

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