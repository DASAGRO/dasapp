package kz.das.dasaccounting.ui.parent_bottom.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import kz.das.dasaccounting.domain.data.Profile
import org.koin.core.KoinComponent
import org.koin.core.inject

class ProfileInfoVM: BaseVM(), KoinComponent {

    private val userRepository: UserRepository by inject()

    private val profileLV = SingleLiveEvent<Profile>()
    fun getProfileLV(): LiveData<Profile> = profileLV

    private val profileImageLV = SingleLiveEvent<String>()
    fun getProfileImageLV(): LiveData<String> = profileImageLV

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

    fun updateImagePath(imageUri: Uri) {
        viewModelScope.launch {
            showLoading()
            try {
                withContext(Dispatchers.IO) {
                    val profileImagePath = userRepository.updateProfileImage(imageUri)
                    profileImageLV.postValue(profileImagePath)
                }
            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                hideLoading()
            }
        }
    }

}