package kz.das.dasaccounting.ui.auth.password_reset

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.AuthRepository
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.inject

class PassResetVM(val profile: Profile): BaseVM() {

    private val authRepository: AuthRepository by inject()
    private val userRepository: UserRepository by inject()

    private val remainedTimeLV: MutableLiveData<Long> = MutableLiveData()
    fun getRemainedTime(): LiveData<Long> = remainedTimeLV

    private val timeFinishedLV: MutableLiveData<Boolean> = MutableLiveData()
    fun isTimerFinished(): LiveData<Boolean> = timeFinishedLV

    init {
        startTimer()
    }

    private val countDownTimer = object : CountDownTimer(
        TIME_IN_MILLIS,
        INTERVAL
    ) {
        override fun onFinish() {
            timeFinishedLV.value = true
        }

        override fun onTick(remainTimeInMillis: Long) {
            remainedTimeLV.value = remainTimeInMillis / 1000
        }
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
            } catch (t: Throwable) {
                throwableHandler.handle(t)
                isLoginConfirmedLV.postValue(false)
            } finally {
                hideLoading()
            }
        }
    }


    private fun startTimer() {
        timeFinishedLV.value = false
        countDownTimer.start()
    }

    fun restartTime() {
        startTimer()
    }

    companion object {
        private const val TIME_IN_MILLIS: Long = 60 * 1000
        private const val INTERVAL: Long = 1000
        private const val CODE_LENGTH = 6
    }


}