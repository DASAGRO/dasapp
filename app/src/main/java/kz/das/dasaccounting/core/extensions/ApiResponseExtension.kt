package kz.das.dasaccounting.core.extensions

import retrofit2.Response
import kz.das.dasaccounting.core.ui.utils.exceptions.BackendResponseException
import kz.das.dasaccounting.core.ui.utils.exceptions.NetworkResponseException
import kz.das.dasaccounting.core.ui.utils.exceptions.NullResponseException


fun <T1, T2> Response<T1>.unwrap(converter: (T1) -> T2): T2 {
    if (!isSuccessful) {
        throw NetworkResponseException(this.errorBody()?.string()?.toStringListApiError()?.descr ?: "Ошибка", code())
    } else if (code() != 200 && code() < 500) {
        throw BackendResponseException(code(), if (code() < 500) message() ?: "" else "")
    } else if (body() == null) {
        throw NullResponseException()
    } else {
        return converter(body()!!)
    }
}

fun <T> Response<T>.unwrap(): T {
    if (!isSuccessful) {
        throw NetworkResponseException(this.errorBody()?.string()?.toStringListApiError()?.descr ?: "Ошибка", code())
    } else if (code() != 200 && code() < 500) {
        throw BackendResponseException(code(), if (code() < 500) message() ?: "" else "")
    } else if (body() == null) {
        throw NullResponseException()
    } else {
        return body()!!
    }
}
