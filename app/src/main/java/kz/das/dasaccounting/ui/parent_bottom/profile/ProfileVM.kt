package kz.das.dasaccounting.ui.parent_bottom.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.utils.InternetAccess
import org.koin.core.KoinComponent

class ProfileVM: BaseVM(), KoinComponent {

    private val profileLV = SingleLiveEvent<Profile>()
    fun getProfileLV(): LiveData<Profile> = profileLV

    init {
        refresh()
    }

    private fun refresh() {
        getProfileLocally()
        getProfile()
    }

    private fun getProfileLocally() {
        profileLV.postValue(userRepository.getUser())
    }

    private fun getProfile() {
        if (InternetAccess.internetCheck(context)) {
            viewModelScope.launch {
                showLoading()
                try {
                    val profile = userRepository.getUserProfile()
                    profile?.let {
                        profileLV.postValue(it)
                        userRepository.setUser(it)
                    }
                } catch (t: Throwable) {
                    throwableHandler.handle(t)
                } finally {
                    hideLoading()
                }
            }
        }
    }

}