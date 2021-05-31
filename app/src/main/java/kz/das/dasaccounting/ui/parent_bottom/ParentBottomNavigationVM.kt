package kz.das.dasaccounting.ui.parent_bottom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ParentBottomNavigationVM: BaseVM(), KoinComponent {

    private val userRepository: UserRepository by inject()

    private val isControlOptionsShowLV = MutableLiveData<Boolean>()
    fun isControlOptionsShow(): LiveData<Boolean> = isControlOptionsShowLV

    fun setControlOptionsState(isShow: Boolean) = isControlOptionsShowLV.postValue(isShow)


}