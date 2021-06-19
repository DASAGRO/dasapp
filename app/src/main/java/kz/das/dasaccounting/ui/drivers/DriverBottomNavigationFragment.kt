package kz.das.dasaccounting.ui.drivers

import android.os.Bundle
import android.view.View
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.databinding.FragmentNavBarParentBinding
import kz.das.dasaccounting.ui.parent_bottom.CoreBottomNavigationFragment

class DriverBottomNavigationFragment: CoreBottomNavigationFragment() {

    companion object {
        fun getScreen() = DasAppScreen(DriverBottomNavigationFragment())
    }

    override fun setupUI() {
        super.setupUI()

    }

    override fun getViewBinding(): FragmentNavBarParentBinding {
        return super.getViewBinding()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}