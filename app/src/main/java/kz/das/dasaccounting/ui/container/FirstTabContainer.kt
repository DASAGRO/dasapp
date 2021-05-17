package kz.das.dasaccounting.ui.container

import android.os.Bundle
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.ui.location.LocationFragment

class FirstTabContainer: BaseFragmentContainer() {

    companion object {
        const val TAG = "FirstTabContainerTag"
        fun newInstance(): FirstTabContainer {
            return FirstTabContainer()
        }
    }

    override fun getInitialFragmentScreen(): DasAppScreen {
        return LocationFragment.getScreen()
    }

}