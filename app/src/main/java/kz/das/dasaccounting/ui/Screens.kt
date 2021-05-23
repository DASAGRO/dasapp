package kz.das.dasaccounting.ui

import kz.das.dasaccounting.ui.parent_bottom.location.LocationFragment
import kz.das.dasaccounting.ui.parent_bottom.profile.ProfileFragment
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


    public enum class ScreenLinks {
        profile,
        location
    }
}