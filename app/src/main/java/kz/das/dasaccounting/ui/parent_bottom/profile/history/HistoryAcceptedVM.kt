package kz.das.dasaccounting.ui.parent_bottom.profile.history

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.inject

class HistoryAcceptedVM: BaseVM() {

    private val userRepository: UserRepository by inject()

    init {

    }

    private fun retrieve() {
        viewModelScope.launch {
            try {

            } catch (t: Throwable) {

            }
        }
    }
}