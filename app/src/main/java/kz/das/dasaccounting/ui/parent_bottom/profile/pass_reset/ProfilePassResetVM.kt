package kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.utils.AppConstants
import org.koin.core.KoinComponent

class ProfilePassResetVM: BaseVM(), KoinComponent {

    private val isValidPasswordLV = SingleLiveEvent<Boolean>()
    fun isValidPassword(): LiveData<Boolean> = isValidPasswordLV

    fun checkPassword(password: String) {
        viewModelScope.launch {
            showLoading()
            try {
                val pass = userRepository.checkPassword(password)
                isValidPasswordLV.postValue(true)
            } catch (t: Throwable) {
                isValidPasswordLV.postValue(false)
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }

    fun checkStaticPass(password: String) = password == AppConstants.STATIC_PASSWORD
}