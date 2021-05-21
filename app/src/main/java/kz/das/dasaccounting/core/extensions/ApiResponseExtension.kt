package kz.das.dasaccounting.core.extensions

import retrofit2.Response
import kz.das.dasaccounting.core.ui.utils.exceptions.BackendResponseException
import kz.das.dasaccounting.core.ui.utils.exceptions.NetworkResponseException
import kz.das.dasaccounting.core.ui.utils.exceptions.NullResponseException

data class ApiResponse<T>(
    val data: T? = null,
) {
    val isSuccessful: Boolean
        get() = data != null
}

fun <T1, T2> Response<ApiResponse<T1>>.unwrap(converter: (T1) -> T2): T2 {
    if (!isSuccessful) {
        throw NetworkResponseException(message(), code())
    } else if (body()?.isSuccessful != true) {
        throw BackendResponseException(code(), if (code() < 500) message() ?: "" else "")
    } else if (body()?.data == null) {
        throw NullResponseException()
    } else {
        return converter(body()?.data!!)
    }
}

fun <T1, T2> Response<ApiResponse<T1>>.unwrap(converter: (T1) -> T2, onResultSaveCallback: onResultSaveCallback<T1>): T2 {
    if (!isSuccessful) {
        throw NetworkResponseException(message(), code())
    } else if (body()?.isSuccessful != true) {
        throw BackendResponseException(code(), if (code() < 500) message() ?: "" else "")
    } else if (body()?.data == null) {
        throw NullResponseException()
    } else {
        onResultSaveCallback.onSaveData(body()?.data!!)
        onResultSaveCallback.onRetrieveData()
        return converter(body()?.data!!)
    }
}

fun <T> Response<ApiResponse<T>>.unwrap(): T {
    if (!isSuccessful) {
        throw NetworkResponseException(message(), code())
    } else if (body()?.isSuccessful != true) {
        throw BackendResponseException(code(), if (code() < 500) message() ?: "" else "")
    } else if (body()?.data == null) {
        throw NullResponseException()
    } else {
        return body()?.data!!
    }
}

interface onResultSaveCallback<T> {
    fun onSaveData(data: T)
    fun onRetrieveData()
}