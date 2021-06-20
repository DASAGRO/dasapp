package kz.das.dasaccounting.ui

import kz.das.dasaccounting.domain.common.UserRole
import kz.das.dasaccounting.ui.drivers.DriverBottomNavigationFragment
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
            UserRole.DRIVER.role to DriverBottomNavigationFragment.getScreen(),
            UserRole.OFFICE.role to OfficeBottomNavigationFragment.getScreen(),
            UserRole.SECURITY.role to GuardBottomNavigationFragment.getScreen(),
            UserRole.WAREHOUSE.role to WarehouseBottomNavigationFragment.getScreen()
        )[userRole]
    }

    public enum class ScreenLinks {
        profile,
        location
    }
}