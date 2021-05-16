package kz.das.dasaccounting.core.ui.utils

import kz.das.dasaccounting.core.ui.utils.exceptions.BackendResponseException
import kz.das.dasaccounting.core.ui.utils.exceptions.NetworkResponseException
import kz.das.dasaccounting.core.ui.utils.exceptions.NullResponseException
import java.lang.RuntimeException
import java.net.UnknownHostException

class UIThrowableHandler(private val callbackListener: Callback? = null) : ThrowableHandler {

    override fun handle(throwable: Throwable) {
        when (throwable) {
            is RuntimeException -> {
                handle(throwable.cause ?: Throwable())
            }
            is UnknownHostException -> {
                callbackListener?.onNoConnectionError()
            }
            is BackendResponseException -> {
                callbackListener?.onBackendResponseError(throwable.errorCode, throwable.message)
            }
            is NullResponseException -> {
                callbackListener?.onNullResponseError()
            }
            is NetworkResponseException -> {
                callbackListener?.onNetworkResponseError(throwable.message)
            }
            else -> {
                callbackListener?.onUnknownError(throwable.message)
            }
        }
    }

    interface Callback {
        fun onNoConnectionError()
        fun onBackendResponseError(errorCode: Int, message: String?)
        fun onUnknownError(message: String?)
        fun onNullResponseError()
        fun onNetworkResponseError(message: String?)
    }
}