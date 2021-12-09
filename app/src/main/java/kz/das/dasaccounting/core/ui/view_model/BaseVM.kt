package kz.das.dasaccounting.core.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.gson.Gson
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.data.ExceptionModel
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.UIThrowableHandler
import kz.das.dasaccounting.data.source.workmanagers.AwaitRequestWorker
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.common.InventoryType
import kz.das.dasaccounting.domain.data.action.InventoryRetain
import kz.das.dasaccounting.domain.data.drivers.FligelProduct
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory
import kz.das.dasaccounting.utils.AppConstants
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit

abstract class BaseVM : ViewModel(), KoinComponent {
    protected val userRepository: UserRepository by inject()
    protected val context: Context by inject()

    private val _isLoading: MutableLiveData<Boolean> = SingleLiveEvent()
    fun isLoading(): LiveData<Boolean> = _isLoading

    private val navigateBackLV: MutableLiveData<Boolean> = SingleLiveEvent()
    fun toNavigateBack(): LiveData<Boolean> = navigateBackLV

    protected val throwableHandler = UIThrowableHandler(object : UIThrowableHandler.Callback {
        override fun onNoConnectionError() {
            _connectionErrorLV.value = true
        }

        override fun onBackendResponseError(errorCode: Int, message: String?) {
            _backendResponseErrorLV.value = ExceptionModel(errorCode, message)
        }

        override fun onUnknownError(message: String?) {
            _unknownErrorLV.value = ExceptionModel(message = message)
        }

        override fun onUiResponseError(message: String?) {
            _unknownErrorLV.value = ExceptionModel(message = message)
        }

        override fun onNullResponseError() {
            _nullResponseErrorLV.value = ExceptionModel()
        }

        override fun onNetworkResponseError(message: String?) {
            _networkResponseErrorLV.value = ExceptionModel(message = message)
        }
    })

    private val _connectionErrorLV = SingleLiveEvent<Boolean>()
    fun getConnectionError(): LiveData<Boolean> = _connectionErrorLV

    private val _backendResponseErrorLV = SingleLiveEvent<ExceptionModel>()
    fun getBackendResponseError(): LiveData<ExceptionModel> = _backendResponseErrorLV

    private val _unknownErrorLV = SingleLiveEvent<ExceptionModel>()
    fun getUnknownError(): LiveData<ExceptionModel> = _unknownErrorLV

    private val _uiErrorLV = SingleLiveEvent<ExceptionModel>()
    fun getUiError(): LiveData<ExceptionModel> = _uiErrorLV

    private val _nullResponseErrorLV = SingleLiveEvent<ExceptionModel>()
    fun getNullResponseError(): LiveData<ExceptionModel> = _nullResponseErrorLV

    private val _networkResponseErrorLV = SingleLiveEvent<ExceptionModel>()
    fun getNetworkResponseError(): LiveData<ExceptionModel> = _networkResponseErrorLV

    protected fun showLoading() {
        _isLoading.value = true
    }

    protected fun hideLoading() {
        _isLoading.value = false
    }

    protected open fun isBackPressHandled(): Boolean {
        return false
    }

    protected fun exit() {
        if (!isBackPressHandled()) {
            navigateBack()
        }
    }

    fun isHaveSavedInventory() = userRepository.getSavedInventory() != null

    fun getSavedInventory() = userRepository.getSavedInventory()

    fun getSavedInventoryInfo(): HashMap<String, Any?> {
        val gson = Gson()
        val inventoryInfo: HashMap<String, Any?> = HashMap()

        userRepository.getSavedInventory()?.let {
            when (it.type) {
                InventoryType.OFFICE -> {
                    val inventory = gson.fromJson(it.body, OfficeInventory::class.java)
                    inventoryInfo["name"] = inventory.name
                    inventoryInfo["imgRes"] = R.drawable.ic_inventory
                    inventoryInfo["desc"] = (context.getString(R.string.inventory_total_quantity) +
                            " " + inventory.quantity +
                            " " + it.type)
                }

                InventoryType.TRANSPORT -> {
                    val inventory = gson.fromJson(it.body, TransportInventory::class.java)
                    inventoryInfo["name"] = inventory.model
                    inventoryInfo["imgRes"] = R.drawable.ic_tractor
                    inventoryInfo["desc"] = (context.getString(R.string.gov_number) + " " + inventory.stateNumber)
                }

                InventoryType.FLIGER -> {
                    val inventory = gson.fromJson(it.body, FligelProduct::class.java)
                    inventoryInfo["imgRes"] = R.drawable.ic_tractor
                    inventoryInfo["desc"] = ("Номер комбайна: " + inventory.combinerNumber + "\n" +
                            "Номер поля: " + inventory.fieldNumber + "\n" +
                            "Вес урожая: " + inventory.harvestWeight)
                }

                InventoryType.WAREHOUSE -> {
                    val inventory = gson.fromJson(it.body, WarehouseInventory::class.java)
                    inventoryInfo["name"] = inventory.name
                    inventoryInfo["imgRes"] = R.drawable.ic_warehouse
                    inventoryInfo["desc"] = (context.getString(R.string.seal_number) + " " + inventory.sealNumber)
                }
            }
        }
        return inventoryInfo
    }

    fun saveInventory(inventory: Any, inventoryType: InventoryType) {
        when(inventoryType) {
            InventoryType.OFFICE -> {
                userRepository.saveInventory(
                        InventoryRetain(inventoryType, Gson().toJson(inventory as OfficeInventory))
                )
            }

            InventoryType.TRANSPORT -> {
                userRepository.saveInventory(
                        InventoryRetain(inventoryType, Gson().toJson(inventory as TransportInventory))
                )
            }

            InventoryType.FLIGER -> {
                userRepository.saveInventory(
                        InventoryRetain(inventoryType, Gson().toJson(inventory as FligelProduct))
                )
            }

            InventoryType.WAREHOUSE -> {
                userRepository.saveInventory(
                        InventoryRetain(inventoryType, Gson().toJson(inventory as WarehouseInventory))
                )
            }
        }
    }

    fun getSavedInventory(inventoryType: InventoryType): Any? {
        userRepository.getSavedInventory()?.let {
            when (inventoryType) {
                InventoryType.OFFICE -> {
                    return Gson().fromJson(it.body, OfficeInventory::class.java)
                }

                InventoryType.TRANSPORT -> {
                    return Gson().fromJson(it.body, TransportInventory::class.java)
                }

                InventoryType.WAREHOUSE -> {
                    return Gson().fromJson(it.body, WarehouseInventory::class.java)
                }

                InventoryType.FLIGER -> {
                    return Gson().fromJson(it.body, FligelProduct::class.java)
                }

                else -> return null
            }
        } ?: run { return null }
    }

    fun deleteSavedInventory() = userRepository.deleteSavedInventory()

    fun startAwaitRequestWorker() {
        val workRequest = OneTimeWorkRequest.Builder(AwaitRequestWorker::class.java)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 2, TimeUnit.MINUTES)
            .addTag(AppConstants.AWAIT_INVENTORY_WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            AppConstants.AWAIT_INVENTORY_WORK_TAG,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun navigateBack() {
        navigateBackLV.value = true
    }
}