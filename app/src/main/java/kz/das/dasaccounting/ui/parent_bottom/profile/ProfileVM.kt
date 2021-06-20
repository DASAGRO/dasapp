package kz.das.dasaccounting.ui.parent_bottom.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileVM: BaseVM(), KoinComponent {

    private val userRepository: UserRepository by inject()

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