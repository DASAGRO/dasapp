package kz.das.dasaccounting.di

import kz.das.dasaccounting.core.ui.shared.NetworkConnectionVM
import kz.das.dasaccounting.ui.ParentBottomNavigationVM
import kz.das.dasaccounting.ui.SplashVM
import kz.das.dasaccounting.ui.auth.login.LoginVM
import kz.das.dasaccounting.ui.auth.login.PassEnterVM
import kz.das.dasaccounting.ui.auth.onboarding.OnBoardingVM
import kz.das.dasaccounting.ui.auth.password_reset.PassResetVM
import kz.das.dasaccounting.ui.auth.password_reset.PhonePassResetVM
import kz.das.dasaccounting.ui.location.LocationVM
import kz.das.dasaccounting.ui.profile.ProfileInfoVM
import kz.das.dasaccounting.ui.profile.ProfileVM
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal fun getAuthViewModels() = module {
    viewModel { SplashVM() }
    viewModel { LoginVM() }
    viewModel { PassEnterVM() }
    viewModel { OnBoardingVM() }
    viewModel { PassResetVM() }
    viewModel { PhonePassResetVM() }
}

internal fun getMainViewModels() = module {
    viewModel { ProfileVM() }
    viewModel { ProfileInfoVM() }
    viewModel { ParentBottomNavigationVM() }
}

internal fun getLocationViewModels() = module {
    viewModel { LocationVM() }
}

internal fun getConfigViewModels() = module {
    viewModel { NetworkConnectionVM() }
}