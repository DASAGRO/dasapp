package kz.das.dasaccounting.ui

import kz.das.dasaccounting.core.ui.view_model.BaseVM
import kz.das.dasaccounting.domain.UserRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class SplashVM: BaseVM(), KoinComponent {

    private val userRepository: UserRepository by inject()

    fun isUserOnSession() = userRepository.isUserOnSession()

}