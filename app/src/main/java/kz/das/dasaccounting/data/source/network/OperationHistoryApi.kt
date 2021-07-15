package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.data.entities.history.HistoryOfficeInventoryEntity
import kz.das.dasaccounting.data.entities.history.HistoryTransportInventoryEntity
import kz.das.dasaccounting.data.entities.history.HistoryWarehouseInventoryEntity
import retrofit2.Response
import retrofit2.http.GET

interface OperationHistoryApi {

    @GET("/api/materials/history/get/po")
    suspend fun getHistoryTransportAccessories(): Response<List<HistoryTransportInventoryEntity>>

    @GET("/api/materials/history/get/ts")
    suspend fun getHistoryTransports(): Response<List<HistoryTransportInventoryEntity>>

    @GET("/api/materials/history/get/tmc")
    suspend fun getHistoryOfficeInventories(): Response<List<HistoryOfficeInventoryEntity >>

    @GET("/api/materials/history/get/storage")
    suspend fun getHistoryWarehouses(): Response<List<HistoryWarehouseInventoryEntity>>

}
