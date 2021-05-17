package kz.das.dasaccounting.ui

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.RouterProvider
import kz.das.dasaccounting.core.navigation.ScreenNavigator
import kz.das.dasaccounting.core.navigation.container.AppRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentNavBarParentBinding
import kz.das.dasaccounting.ui.container.AppNavigator
import kz.das.dasaccounting.ui.container.FirstTabContainer
import kz.das.dasaccounting.ui.container.SecondTabContainer
import org.koin.android.viewmodel.ext.android.viewModel
import ru.terrakok.cicerone.Cicerone

class ParentBottomNavigationFragment: BaseFragment<ParentBottomNavigationVM, FragmentNavBarParentBinding>(), RouterProvider {

    companion object {
        fun getScreen() = DasAppScreen(ParentBottomNavigationFragment())
    }

    override val mViewModel: ParentBottomNavigationVM by viewModel()

    private val cicerone = Cicerone.create(AppRouter())

    private val appNavigator: AppNavigator by lazy {
            AppNavigator(requireActivity(), childFragmentManager, R.id.navContainer)
    }

    override fun getRouter() = cicerone.router

    override fun getViewBinding() = FragmentNavBarParentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appNavigator.initContainers()

        if (savedInstanceState == null) {
            val screen = DasAppScreen(FirstTabContainer.newInstance())
            getRouter().replaceTab(screen)
        }

        mViewBinding.bottomNavigationView.background = null
        mViewBinding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home-> {
                    getRouter().replaceTab(DasAppScreen(FirstTabContainer.newInstance()))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.profile -> {
                    getRouter().replaceTab(DasAppScreen(SecondTabContainer.newInstance()))
                    return@setOnNavigationItemSelectedListener true
                } else -> { return@setOnNavigationItemSelectedListener false }
            }
        }
    }

    override fun setupUI() { }

    override fun onResume() {
        super.onResume()
        cicerone.navigatorHolder.setNavigator(appNavigator)
    }

    override fun onPause() {
        super.onPause()
        cicerone.navigatorHolder.removeNavigator()
    }


}