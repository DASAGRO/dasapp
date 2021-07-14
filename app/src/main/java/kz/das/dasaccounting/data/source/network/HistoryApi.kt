package kz.das.dasaccounting.data.source.network

import retrofit2.http.GET

interface HistoryApi {

    @GET("/api/materials/history/get/po")
    suspend fun getHistoryTransportAccessories()

    @GET("/api/materials/history/get/ts")
    suspend fun getHistoryTransports()

    @GET("/api/materials/history/get/tmc")
    suspend fun getHistoryOfficeInventories()

    @GET("/api/materials/history/get/storage")
    suspend fun getHistoryWarehouses()

}