package kz.das.dasaccounting.di

import kz.das.dasaccounting.MainVM
import kz.das.dasaccounting.core.ui.shared.NetworkConnectionVM
import kz.das.dasaccounting.domain.data.Profile
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationVM
import kz.das.dasaccounting.ui.SplashVM
import kz.das.dasaccounting.ui.auth.login.LoginVM
import kz.das.dasaccounting.ui.auth.login.PassEnterVM
import kz.das.dasaccounting.ui.auth.onboarding.OnBoardingVM
import kz.das.dasaccounting.ui.auth.password_reset.PassResetVM
import kz.das.dasaccounting.ui.auth.password_reset.PhonePassResetVM
import kz.das.dasaccounting.ui.drivers.DriverBottomNavigationVM
import kz.das.dasaccounting.ui.guards.GuardBottomNavigationVM
import kz.das.dasaccounting.ui.office.OfficeBottomNavigationVM
import kz.das.dasaccounting.ui.office.accept.AcceptConfirmationVM
import kz.das.dasaccounting.ui.office.accept.AcceptInventoryInfoVM
import kz.das.dasaccounting.ui.office.transfer.TransferConfirmVM
import kz.das.dasaccounting.ui.office.transfer.TransferFormalizeVM
import kz.das.dasaccounting.ui.office.transfer.TransferVM
import kz.das.dasaccounting.ui.parent_bottom.location.LocationVM
import kz.das.dasaccounting.ui.parent_bottom.profile.history.ProfileHistoryVM
import kz.das.dasaccounting.ui.parent_bottom.profile.ProfileInfoVM
import kz.das.dasaccounting.ui.parent_bottom.profile.support.ProfileSupportVM
import kz.das.dasaccounting.ui.parent_bottom.profile.ProfileVM
import kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset.ProfilePassResetConfirmVM
import kz.das.dasaccounting.ui.parent_bottom.profile.pass_reset.ProfilePassResetVM
import kz.das.dasaccounting.ui.warehouse.WarehouseBottomNavigationVM
import kz.das.dasaccounting.ui.warehouse.operations.WarehouseDetailVM
import kz.das.dasaccounting.ui.warehouse.operations.WarehouseOperationsVM
import kz.das.dasaccounting.ui.warehouse.operations.WarehouseOptionsVM
import kz.das.dasaccounting.ui.warehouse.operations.WarehouseTransferPickVM
import kz.das.dasaccounting.ui.warehouse.transfer.TransferAdditionalVM
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal fun getAuthViewModels() = module {
    viewModel { SplashVM() }
    viewModel { LoginVM() }
    viewModel { (profile: Profile) -> PassEnterVM(profile) }
    viewModel { OnBoardingVM() }
    viewModel { (profile: Profile) -> PassResetVM(profile) }
    viewModel { PhonePassResetVM() }
}

internal fun getMainViewModels() = module {
    viewModel { MainVM() }
    viewModel { ProfileVM() }
    viewModel { ProfileInfoVM() }
    viewModel { CoreBottomNavigationVM() }
    viewModel { GuardBottomNavigationVM() }
    viewModel { DriverBottomNavigationVM() }
    viewModel { WarehouseBottomNavigationVM() }
}

internal fun getOfficeViewModels() = module {
    viewModel { OfficeBottomNavigationVM() }
    viewModel { AcceptInventoryInfoVM() }
    viewModel { AcceptConfirmationVM() }
    viewModel { TransferVM() }
    viewModel { TransferFormalizeVM() }
    viewModel { TransferConfirmVM() }
}

internal fun getDriverViewModels() = module {
    viewModel { kz.das.dasaccounting.ui.drivers.accept.AcceptInventoryInfoVM() }
    viewModel { kz.das.dasaccounting.ui.drivers.accept.AcceptTransportConfirmationVM() }
    viewModel { kz.das.dasaccounting.ui.drivers.transfer.TransferConfirmVM() }
    viewModel { kz.das.dasaccounting.ui.drivers.transfer.TransferFormalizeVM() }
    viewModel { kz.das.dasaccounting.ui.drivers.fligel.TransferConfirmFligelDataVM() }
    viewModel { kz.das.dasaccounting.ui.drivers.fligel.TransferFligelDataInputVM() }
    viewModel { kz.das.dasaccounting.ui.drivers.fligel.TransferFligeDataFormalizeVM() }
}

internal fun getWarehouseViewModels() = module {
    viewModel { kz.das.dasaccounting.ui.warehouse.accept.AcceptConfirmationVM() }
    viewModel { kz.das.dasaccounting.ui.warehouse.accept.AcceptInventoryInfoVM() }
    viewModel { WarehouseDetailVM() }
    viewModel { WarehouseOperationsVM() }
    viewModel { WarehouseOptionsVM() }
    viewModel { WarehouseTransferPickVM() }
    viewModel { TransferAdditionalVM() }
    viewModel { kz.das.dasaccounting.ui.warehouse.transfer.TransferConfirmVM() }
    viewModel { kz.das.dasaccounting.ui.warehouse.transfer.TransferFormalizeVM() }
}

internal fun getProfileViewModels() = module {
    viewModel { ProfileSupportVM() }
    viewModel { ProfileHistoryVM() }
    viewModel { ProfilePassResetVM() }
    viewModel { ProfilePassResetConfirmVM() }
}

internal fun getLocationViewModels() = module {
    viewModel { LocationVM() }
}

internal fun getConfigViewModels() = module {
    viewModel { NetworkConnectionVM() }
}