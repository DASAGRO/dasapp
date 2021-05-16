package kz.das.dasaccounting.core.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.das.dasaccounting.core.ui.data.ExceptionModel
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.UIThrowableHandler
import org.koin.core.KoinComponent

abstract class BaseVM : ViewModel(), KoinComponent {

    private val _isLoading: MutableLiveData<Boolean> = SingleLiveEvent()
    fun isLoading(): LiveData<Boolean> = _isLoading

    private val navigateBackLV: MutableLiveData<Boolean> = SingleLiveEvent()
    fun toNavigateBack(): LiveData<Boolean> = navigateBackLV

    protected val throwableHandler = UIThrowableHandler(object : UIThrowableHandler.Callback {
        override fun onNoConnectionError() {
            _connectionErrorLV.value = true
        }

        override fun onBackendResponseError(errorCode: Int, message: String?) {
            _backendResponseErrorLV.value = ExceptionModel(errorCode, message)
        }

        override fun onUnknownError(message: String?) {
            _unknownErrorLV.value = ExceptionModel(message = message)
        }

        override fun onNullResponseError() {
            _nullResponseErrorLV.value = ExceptionModel()
        }

        override fun onNetworkResponseError(message: String?) {
            _networkResponseErrorLV.value = ExceptionModel(message = message)
        }
    })

    private val _connectionErrorLV = SingleLiveEvent<Boolean>()
    fun getConnectionError(): LiveData<Boolean> = _connectionErrorLV

    private val _backendResponseErrorLV = SingleLiveEvent<ExceptionModel>()
    fun getBackendResponseError(): LiveData<ExceptionModel> = _backendResponseErrorLV

    private val _unknownErrorLV = SingleLiveEvent<ExceptionModel>()
    fun getUnknownError(): LiveData<ExceptionModel> = _unknownErrorLV

    private val _nullResponseErrorLV = SingleLiveEvent<ExceptionModel>()
    fun getNullResponseError(): LiveData<ExceptionModel> = _nullResponseErrorLV

    private val _networkResponseErrorLV = SingleLiveEvent<ExceptionModel>()
    fun getNetworkResponseError(): LiveData<ExceptionModel> = _networkResponseErrorLV

    protected fun showLoading() {
        _isLoading.value = true
    }

    protected fun hideLoading() {
        _isLoading.value = false
    }

    protected open fun isBackPressHandled(): Boolean {
        return false
    }

    protected fun exit() {
        if (!isBackPressHandled()) {
            navigateBack()
        }
    }

    fun navigateBack() {
        navigateBackLV.value = true
    }
}