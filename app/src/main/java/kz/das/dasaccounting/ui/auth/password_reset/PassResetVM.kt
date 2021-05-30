package kz.das.dasaccounting.ui.auth.password_reset

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.exceptions.NetworkResponseException
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.AuthRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.KoinComponent
import org.koin.core.inject

class PassResetVM(val profile: Profile): BaseVM(), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val userRepository: UserRepository by inject()

    private val remainedTimeLV: MutableLiveData<Long> = MutableLiveData()
    fun getRemainedTime(): LiveData<Long> = remainedTimeLV

    private val timeFinishedLV: MutableLiveData<Boolean> = MutableLiveData()
    fun isTimerFinished(): LiveData<Boolean> = timeFinishedLV

    private val _isPasswordSentLV = MutableLiveData<Boolean>()
    fun isPasswordSent() = _isPasswordSentLV

    fun getUserRole() = userRepository.getUserRole()

    private val countDownTimer = object : CountDownTimer(
        60 * 1000,
        1000
    ) {
        override fun onFinish() {
            timeFinishedLV.value = true
        }

        override fun onTick(remainTimeInMillis: Long) {
            remainedTimeLV.value = remainTimeInMillis / 1000
        }
    }

    init {
        startTimer()
    }

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
            }  catch (t: Throwable) {
                if (t is NetworkResponseException && t.httpResponseCode == 400) {
                    isLoginConfirmedLV.postValue(false)
                } else {
                    throwableHandler.handle(t)
                }
            } finally {
                hideLoading()
            }
        }
    }

    fun sendPassword() {
        viewModelScope.launch {
            showLoading()
            try {
                authRepository.sendPassword(profile.phone)
                _isPasswordSentLV.postValue(true)
            } catch (t: Throwable) {
                _isPasswordSentLV.postValue(false)
            } finally {
                hideLoading()
            }
        }
    }

    private fun startTimer() {
        timeFinishedLV.value = false
        countDownTimer.start()
        sendPassword()
    }

    fun restartTime() {
        startTimer()
    }

}