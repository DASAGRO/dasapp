package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.data.entities.driver.TransportInventoryEntity
import kz.das.dasaccounting.data.entities.office.OfficeInventoryEntity
import kz.das.dasaccounting.data.entities.requests.GetStoreRequest
import kz.das.dasaccounting.data.entities.requests.SendStoreRequest
import kz.das.dasaccounting.data.entities.warehouse.WarehouseInventoryEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WarehouseOperationApi {

    @GET("/api/storages")
    suspend fun getWarehouses(): Response<List<WarehouseInventoryEntity>>

    @GET("/api/storages/tmc")
    suspend fun getWarehousesOfficeInventories(@Query("storageUUID") storageUUID: String): Response<List<OfficeInventoryEntity>>

    @GET("/api/storages/ts")
    suspend fun getWarehousesTransportInventories(@Query("storageUUID") storageUUID: String): Response<List<TransportInventoryEntity>>

    @GET("/api/storages/po")
    suspend fun getWarehousesTransportAccessoriesInventories(@Query("storageUUID") storageUUID: String): Response<List<TransportInventoryEntity>>



    @POST("/api/goods/get/store")
    suspend fun getWarehouse(@Body getStoreRequest: GetStoreRequest): Response<Any>

    @POST("/api/goods/send/store")
    suspend fun sendWarehouse(@Body sendStoreRequest: SendStoreRequest): Response<Any>


}