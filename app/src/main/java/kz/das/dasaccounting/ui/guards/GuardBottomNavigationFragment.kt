package kz.das.dasaccounting.ui.guards

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel

class GuardBottomNavigationFragment: CoreBottomNavigationFragment() {

    companion object {
        fun getScreen() = DasAppScreen(GuardBottomNavigationFragment())
    }

    private val guardBottomNavigationVM: GuardBottomNavigationVM by viewModel()

}