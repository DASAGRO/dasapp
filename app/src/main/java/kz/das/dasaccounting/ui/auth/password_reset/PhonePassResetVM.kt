package kz.das.dasaccounting.ui.auth.password_reset

import androidx.lifecycle.MutableLiveData
import kz.das.dasaccounting.core.ui.extensions.isValidPhoneNumber
import kz.das.dasaccounting.core.ui.extensions.toNetworkFormattedPhone
import kz.das.dasaccounting.core.ui.view_model.BaseVM

class PhonePassResetVM: BaseVM() {

    private var formattedPhoneNumber: String = ""

    private val _isValidPhoneNumberLV = MutableLiveData<Boolean>()
    val isValidPhoneNumberLV = _isValidPhoneNumberLV

    private val _isLoginExistLV = MutableLiveData<Boolean>()
    val isLoginExistLV = _isLoginExistLV


    private fun checkPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isNotBlank() && phoneNumber.length == 18) {
            val formattedNumber = phoneNumber.toNetworkFormattedPhone()
            _isValidPhoneNumberLV.value = formattedNumber.isValidPhoneNumber()
        } else {
            _isValidPhoneNumberLV.value = false
        }
    }

    fun onLoginTextChanged(phoneNumber: String) {
        formattedPhoneNumber = phoneNumber
        checkPhoneNumber(phoneNumber)
    }


}