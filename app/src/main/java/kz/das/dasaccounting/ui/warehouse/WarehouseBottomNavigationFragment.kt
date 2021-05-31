package kz.das.dasaccounting.ui.warehouse

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationFragment

class WarehouseBottomNavigationFragment: CoreBottomNavigationFragment() {

    companion object {
        fun getScreen() = DasAppScreen(WarehouseBottomNavigationFragment())
    }

}