package kz.das.dasaccounting.core.navigation

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Router
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

class ScreenNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager? = null
) : SupportAppNavigator(activity, fragmentManager ?: activity.supportFragmentManager, containerId) {

//    override fun setupFragmentTransaction(
//        command: Command,
//        currentFragment: Fragment?,
//        nextFragment: Fragment?,
//        fragmentTransaction: FragmentTransaction
//    ) {
//        fragmentTransaction.setCustomAnimations(
//            R.anim.slide_in,
//            R.anim.fade_out,
//            R.anim.fade_in,
//            R.anim.slide_out
//        )
//    }
}


class InnerScreenNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager
) : SupportAppNavigator(activity, fragmentManager, containerId) {

}

fun Fragment.requireRouter(): Router {
    var parentFragment = parentFragment

    while (parentFragment != null) {
        val fragmentRouterProvider = parentFragment as? RouterProvider
        if (fragmentRouterProvider != null) {
            return fragmentRouterProvider.getRouter()
        }
        parentFragment = parentFragment.parentFragment
    }

    return requireFlowRouter()
}

fun Fragment.requireFlowRouter(): Router {
    return (requireActivity() as RouterProvider).getRouter()
}