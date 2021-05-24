package kz.das.dasaccounting.di

import kz.das.dasaccounting.core.ui.shared.NetworkConnectionVM
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.ui.parent_bottom.ParentBottomNavigationVM
import kz.das.dasaccounting.ui.SplashVM
import kz.das.dasaccounting.ui.auth.login.LoginVM
import kz.das.dasaccounting.ui.auth.login.PassEnterVM
import kz.das.dasaccounting.ui.auth.onboarding.OnBoardingVM
import kz.das.dasaccounting.ui.auth.password_reset.PassResetVM
import kz.das.dasaccounting.ui.auth.password_reset.PhonePassResetVM
import kz.das.dasaccounting.ui.drivers.DriverBottomNavigationVM
import kz.das.dasaccounting.ui.guards.GuardBottomNavigationVM
import kz.das.dasaccounting.ui.office.OfficeBottomNavigationVM
import kz.das.dasaccounting.ui.parent_bottom.location.LocationVM
import kz.das.dasaccounting.ui.parent_bottom.profile.history.ProfileHistoryVM
import kz.das.dasaccounting.ui.parent_bottom.profile.ProfileInfoVM
import kz.das.dasaccounting.ui.parent_bottom.profile.support.ProfileSupportVM
import kz.das.dasaccounting.ui.parent_bottom.profile.ProfileVM
import kz.das.dasaccounting.ui.warehouse.WarehouseBottomNavigationVM
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal fun getAuthViewModels() = module {
    viewModel { SplashVM() }
    viewModel { LoginVM() }
    viewModel { (profile: Profile) -> PassEnterVM(profile) }
    viewModel { OnBoardingVM() }
    viewModel { PassResetVM() }
    viewModel { PhonePassResetVM() }
}

internal fun getMainViewModels() = module {
    viewModel { ProfileVM() }
    viewModel { ProfileInfoVM() }
    viewModel { ParentBottomNavigationVM() }
    viewModel { OfficeBottomNavigationVM() }
    viewModel { GuardBottomNavigationVM() }
    viewModel { DriverBottomNavigationVM() }
    viewModel { WarehouseBottomNavigationVM() }
}

internal fun getProfileViewModels() = module {
    viewModel { ProfileSupportVM() }
    viewModel { ProfileHistoryVM() }
}

internal fun getLocationViewModels() = module {
    viewModel { LocationVM() }
}

internal fun getConfigViewModels() = module {
    viewModel { NetworkConnectionVM() }
}