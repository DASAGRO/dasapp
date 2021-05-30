package kz.das.dasaccounting.ui.guards

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.ui.parent_bottom.ParentBottomNavigationFragment
import org.koin.android.viewmodel.ext.android.viewModel

class GuardBottomNavigationFragment: ParentBottomNavigationFragment() {

    companion object {
        fun getScreen() = DasAppScreen(GuardBottomNavigationFragment())
    }

    private val guardBottomNavigationVM: GuardBottomNavigationVM by viewModel()

}