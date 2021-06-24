package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.data.entities.driver.DriverInventoryEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DriverOperationApi {


    @GET("/api/transports/get")
    suspend fun getTransports(): Response<List<DriverInventoryEntity>>

    @POST("/api/goods/get/fligel")
    suspend fun acceptInventoryFligel(@Body driverInventoryEntity: DriverInventoryEntity): Response<ApiResponseMessage>

//    @POST("/api/goods/send/fligel")
//    suspend fun sendInventoryFligel(@Body driverInventoryEntity: DriverInventoryEntity): Response<ApiResponseMessage>
//
//    @POST("/api/goods/send/tmc")
//    suspend fun sendInventoryFligel(@Body driverInventoryEntity: DriverInventoryEntity): Response<ApiResponseMessage>
//
//    @POST("/api/goods/get/ts")
//    suspend fun sendInventoryFligel(@Body driverInventoryEntity: DriverInventoryEntity): Response<ApiResponseMessage>


    @POST("/api/goods/send/po")
    suspend fun sendInventoryDriverAccessory(@Body driverInventoryEntity: DriverInventoryEntity): Response<ApiResponseMessage>

    @POST("/api/goods/send/ts")
    suspend fun sendInventoryDriverTransport(@Body driverInventoryEntity: DriverInventoryEntity): Response<ApiResponseMessage>




}