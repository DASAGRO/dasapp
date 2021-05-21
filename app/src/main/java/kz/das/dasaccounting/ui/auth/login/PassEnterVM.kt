package kz.das.dasaccounting.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.AuthRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.KoinComponent
import org.koin.core.inject

class PassEnterVM(val profile: Profile): BaseVM(), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val userRepository: UserRepository by inject()

    private val isLoginConfirmedLV = SingleLiveEvent<Boolean>()
    fun isLoginConfirmed(): LiveData<Boolean> = isLoginConfirmedLV

    fun login(password: String) {
        viewModelScope.launch {
            showLoading()
            try {
                val profile = authRepository.login(profile.phone, password)
                userRepository.updateToken(profile.token ?: "")
                profile.token = ""
                userRepository.setUser(profile)
                isLoginConfirmedLV.postValue(true)
            } catch (t: Throwable) {
                throwableHandler.handle(t)
                isLoginConfirmedLV.postValue(false)
            } finally {
                hideLoading()
            }
        }
    }

}