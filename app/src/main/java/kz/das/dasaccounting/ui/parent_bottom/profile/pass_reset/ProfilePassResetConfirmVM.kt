package kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.exceptions.UiResponseException
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import org.koin.core.KoinComponent

class ProfilePassResetConfirmVM: BaseVM(), KoinComponent {

    private val isPassUpdateLV = SingleLiveEvent<Boolean>()
    fun isPassUpdated(): LiveData<Boolean> = isPassUpdateLV

    fun checkPassword(pass: String, passConfirm: String) {
        when {
            pass.isEmpty() -> {
                throwableHandler.handle(UiResponseException("Введите пароль"))
            }
            pass != passConfirm -> {
                throwableHandler.handle(UiResponseException("Пароли не совпадают!"))
            }
            else -> {
                initPassword(passConfirm)
            }
        }
    }


    private fun initPassword(passConfirm: String) {
        viewModelScope.launch {
            try {
                showLoading()
                userRepository.updatePassword(passConfirm)
                isPassUpdateLV.postValue(true)
            } catch (t: Throwable) {
                isPassUpdateLV.postValue(false)
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }


}