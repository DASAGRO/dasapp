package kz.das.dasaccounting.ui.auth.login

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.utils.exceptions.NetworkResponseException
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.AuthRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.InterruptedIOException

class PassEnterVM(val profile: Profile?): BaseVM(), KoinComponent {

    private val authRepository: AuthRepository by inject()

    private val remainedTimeLV: MutableLiveData<Long> = MutableLiveData()
    fun getRemainedTime(): LiveData<Long> = remainedTimeLV

    private val timeFinishedLV: MutableLiveData<Boolean> = MutableLiveData()
    fun isTimerFinished(): LiveData<Boolean> = timeFinishedLV

    private val isLoginConfirmedLV = SingleLiveEvent<Boolean>()
    fun isLoginConfirmed(): LiveData<Boolean> = isLoginConfirmedLV

    private val isOnBoardingConfirmedLV = SingleLiveEvent<Boolean>()
    fun isOnBoardingConfirmed(): LiveData<Boolean> = isOnBoardingConfirmedLV

    private val _numberAttemptsLV = MutableLiveData<Int?>()
    fun numberAttemptsLV(): LiveData<Int?> = _numberAttemptsLV

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
        if (isTimeEnabled()) {
            startTimer()
        }
    }

    fun login(password: String) {
        viewModelScope.launch {
            showLoading()
            try {
                val onBoardingRequired: Boolean = profile?.hasPassword == false
                val profile = authRepository.login(profile?.phone, password)
                userRepository.updateToken(profile.token ?: "")
                profile.token = ""
                userRepository.setUser(profile)

                userRepository.getHistoryOfficeInventories()
                userRepository.getHistoryTransportInventories()
                userRepository.getHistoryWarehouseInventories()

                userRepository.setHistoryInventoriesLoaded(true)

                isOnBoardingConfirmedLV.postValue(onBoardingRequired)
                if (!onBoardingRequired) {
                    isLoginConfirmedLV.postValue(true)
                }
            } catch (t: Throwable) {
                if (t is NetworkResponseException && t.httpResponseCode == 400) {
                    isLoginConfirmedLV.postValue(false)
                } else if(t is InterruptedIOException) {
                    _numberAttemptsLV.postValue((numberAttemptsLV().value ?: 0) + 1)
                } else {
                    userRepository.setHistoryInventoriesLoaded(false)
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
                authRepository.sendPassword(profile?.phone)
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }

    fun isTimeEnabled() = profile?.hasPassword == false

    private fun startTimer() {
        timeFinishedLV.value = false
        countDownTimer.start()
        if (isTimeEnabled()) {
            sendPassword()
        }
    }

    fun restartTime() {
        startTimer()
    }

}