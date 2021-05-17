package kz.das.dasaccounting.ui.container

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.ui.profile.ProfileFragment

class SecondTabContainer: BaseFragmentContainer() {

    companion object {
        const val TAG = "FirstTabContainerTag"
        fun newInstance(): FirstTabContainer {
            return FirstTabContainer()
        }
    }

    override fun getInitialFragmentScreen(): DasAppScreen {
        return ProfileFragment.getScreen()
    }

}