package kz.das.dasaccounting.data.source.network

import kz.das.dasaccounting.core.extensions.ApiResponseMessage
import kz.das.dasaccounting.data.entities.common.ShiftStateEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ShiftApi {

    /**
     * @author kassiend
     * shift start POST request method
     * */
    @POST("api/shift/open")
    suspend fun startWork(@Body shiftMap: HashMap<String, Any?>): ApiResponseMessage

    /**
     * @author kassiend
     * shift finish POST request method
     * */
    @POST("api/shift/close")
    suspend fun finishWork(@Body shiftMap: HashMap<String, Any?>): Response<ApiResponseMessage>

    /**
     * @author kassiend
     * shift finish POST request method
     * */
    @POST("api/shift/is-opened")
    suspend fun isOnSession(): Response<ShiftStateEntity>
}