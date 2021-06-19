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

class CoreBottomNavigationVM: BaseVM(), KoinComponent {

    private val userRepository: UserRepository by inject()
    private val shiftRepository: ShiftRepository by inject()

    private val isControlOptionsShowLV = MutableLiveData<Boolean>()
    fun isControlOptionsShow(): LiveData<Boolean> = isControlOptionsShowLV

    fun setControlOptionsState(isShow: Boolean) = isControlOptionsShowLV.postValue(isShow)

    fun isOnWork() = userRepository.userOnWork()

    fun startWork() {
        viewModelScope.launch {
            showLoading()
            try {
                shiftRepository.startShift(userRepository.getLastLocation().lat,
                    userRepository.getLastLocation().long,
                    System.currentTimeMillis())
                userRepository.startWork()
                setControlOptionsState(isOnWork())
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }

    fun stopWork() {
        viewModelScope.launch {
            try {
                shiftRepository.finishShift()
                userRepository.stopWork()
                setControlOptionsState(isOnWork())
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }

}