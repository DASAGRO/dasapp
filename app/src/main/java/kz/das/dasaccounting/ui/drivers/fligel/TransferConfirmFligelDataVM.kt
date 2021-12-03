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
import kz.das.dasaccounting.domain.data.office.NomenclatureOfficeInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.core.inject
import java.util.*
import kotlin.collections.ArrayList

class TransferConfirmFligelDataVM: BaseVM() {

    private val driverInventoryRepository: DriverInventoryRepository by inject()
    private val officeInventoryRepository: OfficeInventoryRepository by inject()

    private var fligelProduct: FligelProduct? = null

    private val nomenclatures: ArrayList<NomenclatureOfficeInventory> = arrayListOf()
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

    fun setNomenclatures(nomenclatures: List<NomenclatureOfficeInventory>) {
        this.nomenclatures.clear()
        this.nomenclatures.addAll(nomenclatures)
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
                driverInventoryDataLV.postValue(true)
            } catch (t: Throwable) {
                fligelProduct?.let {
                    driverInventoryRepository.saveAwaitReceiveFligelData(it)
                }
                val nomenclatureOfficeInventory = nomenclatures.find { it.fieldNumber == fligelProduct?.fieldNumber.toString() }
                val constructOfficeInventory = OfficeInventory(
                    id = 1,
                    date = System.currentTimeMillis(),
                    name = nomenclatureOfficeInventory?.name ?: "",
                    humidity = fligelProduct?.humidity,
                    latitude = userRepository.getLastLocation().lat,
                    longitude = userRepository.getLastLocation().long,
                    materialUUID = nomenclatureOfficeInventory?.materialUUID ?: "Not found UUID",
                    senderUUID = userRepository.getUser()?.userId,
                    requestId = UUID.randomUUID().toString(),
                    quantity = fligelProduct?.harvestWeight,
                    type = nomenclatureOfficeInventory?.measurement,
                    syncRequire = 0,
                    senderName = userRepository.getUser()?.firstName + " " + userRepository.getUser()?.lastName,
                    comment = ""
                )
                officeInventoryRepository.saveOfficeInventory(constructOfficeInventory)
                isOnAwaitLV.postValue(true)
                hideLoading()
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

    fun getNomenclaturesLocally() = officeInventoryRepository.getNomenclaturesLocally()

}