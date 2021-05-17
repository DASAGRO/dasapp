package kz.das.dasaccounting.core.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class DasAppScreen(
    private val fragment: Fragment,
    val isAuthRequired: Boolean = false,
    val screenName: String = "screen"
) : SupportAppScreen() {

    fun setArgs(args: Bundle) {
        fragment.arguments = args
    }

    override fun getScreenKey(): String {
        return fragment.javaClass.canonicalName ?: "Unknown fragment name"
    }

    override fun getFragment(): Fragment? {
        return fragment
    }
}