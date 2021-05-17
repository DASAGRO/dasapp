package kz.das.dasaccounting.ui.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.OnBackPressedListener
import kz.das.dasaccounting.core.navigation.RouterProvider
import kz.das.dasaccounting.core.navigation.ScreenNavigator
import kz.das.dasaccounting.core.navigation.container.AppRouter
import kz.das.dasaccounting.core.navigation.container.LocalCiceroneHolder
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Navigator

abstract class BaseFragmentContainer : androidx.fragment.app.Fragment(), OnBackPressedListener, RouterProvider {

    protected abstract fun getInitialFragmentScreen(): DasAppScreen

    private val navigator by lazy {
        ScreenNavigator(requireActivity(), R.id.innerContainer, childFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.findFragmentById(R.id.innerContainer) == null) {
            getRouter().newRootScreen(getInitialFragmentScreen())
        }
    }

    override fun onResume() {
        super.onResume()
        getCicerone().navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        getCicerone().navigatorHolder.removeNavigator()
    }

    override fun onBackPressed(): Boolean {
        val fragment = childFragmentManager.findFragmentById(R.id.innerContainer)
        return if (fragment != null && fragment is OnBackPressedListener && fragment.onBackPressed()) {
            true
        } else {
            (activity as RouterProvider).getRouter().exit()
            true
        }
    }

    fun getContainerName(): String {
        return javaClass.canonicalName ?: "Unknown container name"
    }

    private fun getCicerone(): Cicerone<AppRouter> {
        val containerName = getContainerName()
        return LocalCiceroneHolder.getCicerone(containerName)
    }


    override fun getRouter() = getCicerone().router
}