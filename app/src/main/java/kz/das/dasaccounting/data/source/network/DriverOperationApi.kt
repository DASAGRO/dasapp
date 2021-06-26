package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.data.entities.driver.TransportInventoryEntity
import kz.das.dasaccounting.data.entities.requests.GetTransportRequest
import kz.das.dasaccounting.data.entities.requests.ReceiveFligelDataRequest
import kz.das.dasaccounting.data.entities.requests.SendTransportRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DriverOperationApi {

    @GET("/api/transports/get")
    suspend fun getTransports(): Response<List<TransportInventoryEntity>>

    @POST("/api/goods/receive/fligel")
    suspend fun receiveInventoryFligel(@Body receiveFligelDataRequest: ReceiveFligelDataRequest): Response<ApiResponseMessage>

    @POST("/api/goods/get/ts")
    suspend fun getInventoryDriverTransport(@Body getTransportRequest: GetTransportRequest): Response<Any>

    @POST("/api/goods/send/ts")
    suspend fun sendInventoryDriverTransport(@Body sendTransportRequest: SendTransportRequest): Response<Any>


}