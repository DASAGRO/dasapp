package kz.das.dasaccounting

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import org.koin.core.KoinComponent

class MainVM: BaseVM(), KoinComponent {

    private val isLogoutLV: SingleLiveEvent<Boolean> = SingleLiveEvent()
    fun isLogoutCompleted(): LiveData<Boolean> = isLogoutLV

    fun logOut() {
        userRepository.logOut()
        viewModelScope.launch {
            userRepository.deleteData()
        }
        isLogoutLV.postValue(true)
    }

}