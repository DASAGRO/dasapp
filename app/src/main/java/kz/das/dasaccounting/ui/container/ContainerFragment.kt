package kz.das.dasaccounting.ui.container

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.OnBackPressedListener
import kz.das.dasaccounting.core.navigation.RouterProvider
import kz.das.dasaccounting.core.navigation.ScreenNavigator
import kz.das.dasaccounting.ui.Screens
import kz.das.dasaccounting.ui.location.LocationFragment
import kz.das.dasaccounting.ui.profile.ProfileFragment
import org.koin.core.KoinComponent
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

class ContainerFragment : Fragment(), OnBackPressedListener, RouterProvider, KoinComponent {

    companion object {
        private const val SCREEN_NAME = "SCREEN_NAME"
        fun newInstance(tabName: String) = ContainerFragment().apply {
            arguments = Bundle().apply { putString(SCREEN_NAME, tabName) }
        }
    }

    private val cicerone: Cicerone<Router> = Cicerone.create()
    private val navigator by lazy {
        ScreenNavigator(requireActivity(), R.id.innerContainer, childFragmentManager)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            val rootScreen = when (arguments?.getString(SCREEN_NAME)) {
                Screens.ScreenLinks.profile.toString() -> DasAppScreen(ProfileFragment())
                Screens.ScreenLinks.location.toString() -> DasAppScreen(LocationFragment())
                else -> DasAppScreen(ContainerFragment())
            }
            cicerone.router.newRootScreen(rootScreen)
        }
    }

    override fun onResume() {
        super.onResume()
        cicerone.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        cicerone.navigatorHolder.removeNavigator()
    }

    override fun onBackPressed(): Boolean {
        return if (childFragmentManager.fragments.last() is OnBackPressedListener) {
            true
        } else {
            cicerone.router.exit()
            return true
        }
    }

    override fun getRouter() = cicerone.router
}