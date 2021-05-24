package kz.das.dasaccounting.ui.guards

import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.inject

class GuardBottomNavigationVM: BaseVM() {

    private val userRepository: UserRepository by inject()



}