package kz.das.dasaccounting.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.extensions.isValidPhoneNumber
import kz.das.dasaccounting.core.ui.extensions.toNetworkFormattedPhone
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.exceptions.NetworkResponseException
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.AuthRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.InterruptedIOException

class LoginVM: BaseVM(), KoinComponent {

    private val authRepository: AuthRepository by inject()

    private var formattedPhoneNumber: String = ""

    private fun clearFormattedNumber() {
        formattedPhoneNumber = ""
    }

    private val _isValidPhoneNumberLV = MutableLiveData<Boolean>()
    val isValidPhoneNumberLV = _isValidPhoneNumberLV

    private val _isLoginExistLV = SingleLiveEvent<Profile?>()
    fun isLoginExist(): LiveData<Profile?> = _isLoginExistLV

    private val _numberAttemptsLV = MutableLiveData<Int?>()
    fun numberAttemptsLV(): LiveData<Int?> = _numberAttemptsLV

    private fun checkPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isNotBlank() && phoneNumber.length == 18) {
            val formattedNumber = phoneNumber.toNetworkFormattedPhone()
            _isValidPhoneNumberLV.value = formattedNumber.isValidPhoneNumber()
        } else {
            _isValidPhoneNumberLV.value = false
        }
    }

    fun checkUser() {
        viewModelScope.launch {
            showLoading()
            try {
                val user = authRepository.checkPhone(formattedPhoneNumber.toNetworkFormattedPhone())
                _isLoginExistLV.postValue(user)
            } catch (t: Throwable) {
                if (t is NetworkResponseException && t.httpResponseCode == 400) {
                    _isLoginExistLV.postValue(null)
                } else if(t is InterruptedIOException) {
                    _numberAttemptsLV.postValue((numberAttemptsLV().value?: 0) + 1)
                } else {
                    throwableHandler.handle(t)
                }
            } finally {
                hideLoading()
            }
        }
    }

    fun onLoginTextChanged(phoneNumber: String) {
        formattedPhoneNumber = phoneNumber
        checkPhoneNumber(phoneNumber)
    }

    fun resetNumberAttempts() {
        _numberAttemptsLV.postValue(null)
    }
}