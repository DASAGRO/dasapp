package kz.das.dasaccounting.domain

import androidx.lifecycle.LiveData
import kz.das.dasaccounting.domain.data.drivers.TransportInventory

interface DriverInventoryRepository {
    
    suspend fun getDriverTransports(): List<TransportInventory>

    suspend fun initAwaitAcceptInventory()

    suspend fun initAwaitSendInventory()

    suspend fun acceptInventory(transportInventory: TransportInventory, comment: String, fileIds: ArrayList<Int>): Any

    suspend fun sendInventory(transportInventory: TransportInventory): Any

    suspend fun saveAwaitAcceptInventory(transportInventory: TransportInventory, comment: String, fileIds: ArrayList<Int>)

    suspend fun saveAwaitSentInventory(transportInventory: TransportInventory)

    fun getTransportsLocally(): LiveData<List<TransportInventory>>

    fun getDriverSentMaterialsLocally(): LiveData<List<TransportInventory>>

    fun getDriverAcceptedMaterialsLocally(): LiveData<List<TransportInventory>>
    
}