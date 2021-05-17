package kz.das.dasaccounting.core.navigation.container

import kz.das.dasaccounting.core.navigation.DasAppScreen
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Command

class AppRouter : Router() {

    fun replaceTab(screen: DasAppScreen) {
        val command = ReplaceBottomTab(screen)
        executeCommands(command)
    }

}