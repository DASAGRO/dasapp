package kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfilePassResetVM: BaseVM(), KoinComponent {

    private val userRepository: UserRepository by inject()

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


}