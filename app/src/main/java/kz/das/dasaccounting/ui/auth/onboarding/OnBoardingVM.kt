package kz.das.dasaccounting.ui.auth.onboarding

import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.inject

class OnBoardingVM: BaseVM() {

    private val userRepository: UserRepository by inject()

    fun getUserRole() = userRepository.getUserRole()

}