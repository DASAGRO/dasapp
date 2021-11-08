package kz.das.dasaccounting.ui

import kz.das.dasaccounting.core.ui.view_model.BaseVM
import org.koin.core.KoinComponent

class SplashVM: BaseVM(), KoinComponent {

    fun isUserOnSession() = userRepository.isUserOnSession()

    fun getUserRole() = userRepository.getUserRole()

}