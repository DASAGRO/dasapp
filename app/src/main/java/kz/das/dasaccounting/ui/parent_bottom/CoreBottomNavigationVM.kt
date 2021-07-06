package kz.das.dasaccounting.ui.parent_bottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.ShiftRepository
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class CoreBottomNavigationVM: BaseVM(), KoinComponent {

    private val userRepository: UserRepository by inject()
    private val shiftRepository: ShiftRepository by inject()

    private val isControlOptionsShowLV = MutableLiveData<Boolean>()
    fun isControlOptionsShow(): LiveData<Boolean> = isControlOptionsShowLV

    private val isStartWorkWithQrLV = MutableLiveData<Boolean>()
    fun isStartWorkWithQrLV(): LiveData<Boolean> = isStartWorkWithQrLV

    fun setStartWorkWithQrLV() { isStartWorkWithQrLV.postValue(true) }

    private val isWorkStartedLV = MutableLiveData<Boolean>()
    fun isWorkStarted(): LiveData<Boolean> = isWorkStartedLV

    private val isWorkStoppedLV = MutableLiveData<Boolean>()
    fun isWorkStopped(): LiveData<Boolean> = isWorkStoppedLV

    private val isRefreshLV = MutableLiveData<Boolean>()
    fun isRefresh(): LiveData<Boolean> = isRefreshLV

    fun setRefresh(refresh: Boolean) = isRefreshLV.postValue(refresh)

    fun setControlOptionsState(isShow: Boolean) = isControlOptionsShowLV.postValue(isShow)

    fun getUserRole() = userRepository.getUserRole()

    fun isOnWork() = userRepository.userOnWork()

    init {
        checkShiftState()
    }

    fun startWork() {
        viewModelScope.launch {
            showLoading()
            try {
                shiftRepository.startShift(userRepository.getLastLocation().lat,
                    userRepository.getLastLocation().long,
                    System.currentTimeMillis())
                userRepository.startWork()
                setControlOptionsState(isOnWork())
                isWorkStartedLV.postValue(true)
            } catch (t: Throwable) {
                if (t is SocketTimeoutException
                    || t is UnknownHostException
                    || t is ConnectException
                ) {
                    shiftRepository.saveAwaitStartShift(userRepository.getLastLocation().lat,
                        userRepository.getLastLocation().long,
                        System.currentTimeMillis())
                    setControlOptionsState(isOnWork())
                    isWorkStartedLV.postValue(true)
                } else {
                    throwableHandler.handle(t)
                }
            } finally {
                hideLoading()
            }
        }
    }

    // TODO add request params from userRepository in shiftRepository
    fun startWork(qrScan: String) {
        viewModelScope.launch {
            showLoading()
            try {
                shiftRepository.startShift(userRepository.getLastLocation().lat,
                    userRepository.getLastLocation().long,
                    System.currentTimeMillis(), qrScan)
                userRepository.startWork()
                setControlOptionsState(isOnWork())
                isWorkStartedLV.postValue(true)
            } catch (t: Throwable) {
                if (t is SocketTimeoutException
                    || t is UnknownHostException
                    || t is ConnectException
                ) {
                    shiftRepository.saveAwaitStartShift(userRepository.getLastLocation().lat,
                        userRepository.getLastLocation().long,
                        System.currentTimeMillis(), qrScan)
                    setControlOptionsState(isOnWork())
                    isWorkStartedLV.postValue(true)
                } else {
                    throwableHandler.handle(t)
                }
            } finally {
                hideLoading()
            }
        }
    }

    // TODO add request params from userRepository in shiftRepository
    fun stopWork() {
        viewModelScope.launch {
            try {
                shiftRepository.finishShift(userRepository.getLastLocation().lat,
                    userRepository.getLastLocation().long,
                    System.currentTimeMillis())
                userRepository.stopWork()
                setControlOptionsState(isOnWork())
                isWorkStoppedLV.postValue(true)
            } catch (t: Throwable) {
                if (t is SocketTimeoutException
                    || t is UnknownHostException
                    || t is ConnectException
                ) {
                    shiftRepository.saveAwaitFinishShift(userRepository.getLastLocation().lat,
                        userRepository.getLastLocation().long,
                        System.currentTimeMillis())
                    setControlOptionsState(isOnWork())
                    isWorkStoppedLV.postValue(true)
                } else {
                    throwableHandler.handle(t)
                }
            } finally {
                hideLoading()
            }
        }
    }

    private fun checkShiftState() {
        viewModelScope.launch {
            try {
                val state = shiftRepository.isShiftState().opened
                if (state) {
                    isWorkStartedLV.postValue(true)
                    userRepository.startWork()
                } else {
                    isWorkStoppedLV.postValue(true)
                    userRepository.stopWork()
                }
                setControlOptionsState(state)
            } catch (t: Throwable) {

            }
        }
    }


}