package kz.das.dasaccounting.ui.parent_bottom.profile.support

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.utils.SingleLiveEvent
import kz.das.dasaccounting.core.ui.view_model.BaseVM

class ProfileSupportVM: BaseVM() {

    private val isFilesUploadingLV = SingleLiveEvent<Boolean>()
    fun isFilesUploading(): LiveData<Boolean> = isFilesUploadingLV

    fun upload(list: List<Uri>) {
        viewModelScope.launch {
            isFilesUploadingLV.postValue(true)
            try {


            } catch (t: Throwable) {
                throwableHandler.handle(t)
            } finally {
                isFilesUploadingLV.postValue(false)
            }
        }
    }


}