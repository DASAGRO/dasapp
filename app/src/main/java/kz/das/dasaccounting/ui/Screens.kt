package kz.das.dasaccounting.ui

import kz.das.dasaccounting.domain.common.ProfileRole
import kz.das.dasaccounting.ui.drivers.DriverBottomNavigationFragment
import kz.das.dasaccounting.ui.drivers.DriverBottomNavigationVM
import kz.das.dasaccounting.ui.guards.GuardBottomNavigationFragment
import kz.das.dasaccounting.ui.office.OfficeBottomNavigationFragment
import kz.das.dasaccounting.ui.parent_bottom.location.LocationFragment
import kz.das.dasaccounting.ui.parent_bottom.profile.ProfileFragment
import kz.das.dasaccounting.ui.warehouse.WarehouseBottomNavigationFragment
import ru.terrakok.cicerone.Screen

object Screens {

    fun getLocationScreen(screenName: String): Screen? {
        return if (screenName == ScreenLinks.location.toString()) {
            LocationFragment.getScreen()
        } else {
            null
        }
    }

    fun getProfileScreen(screenName: String): Screen? {
        return if (screenName == ScreenLinks.profile.toString()) {
            ProfileFragment.getScreen()
        } else {
            null
        }
    }

    fun getRoleScreens(userRole: String): Screen? {
        return mapOf(
            ProfileRole.DRIVER.role to DriverBottomNavigationFragment.getScreen(),
            ProfileRole.OFFICE.role to OfficeBottomNavigationFragment.getScreen(),
            ProfileRole.SECURITY.role to GuardBottomNavigationFragment.getScreen(),
            ProfileRole.WAREHOUSE.role to WarehouseBottomNavigationFragment.getScreen()
        )[userRole]
    }

    public enum class ScreenLinks {
        profile,
        location
    }
}