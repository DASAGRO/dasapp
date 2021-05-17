package kz.das.dasaccounting.ui.container

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import kz.das.dasaccounting.core.navigation.container.ReplaceBottomTab
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import java.util.*

class AppNavigator(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        @IdRes containerId: Int
) : SupportAppNavigator(activity, fragmentManager, containerId) {

    constructor(activity: FragmentActivity, @IdRes containerId: Int) : this(activity, activity.supportFragmentManager, containerId)

    private val containers = LinkedList<BaseFragmentContainer>()

    fun initContainers() {
        val fm = fragmentManager ?: return
        val firstTabContainer = fm.findFragmentByTag(FirstTabContainer.TAG) as? FirstTabContainer
                ?: FirstTabContainer.newInstance()
        fm.beginTransaction()
                .replace(containerId, firstTabContainer, FirstTabContainer.TAG)
                .detach(firstTabContainer)
                .commitNow()

        val secondTabContainer = fm.findFragmentByTag(SecondTabContainer.TAG) as? SecondTabContainer
                ?: SecondTabContainer.newInstance()
        fm.beginTransaction()
                .replace(containerId, secondTabContainer, SecondTabContainer.TAG)
                .detach(secondTabContainer)
                .commitNow()

        containers.add(firstTabContainer)
        containers.add(secondTabContainer)
    }

    override fun applyCommand(command: Command) {
        if (command is ReplaceBottomTab) {
            val transaction = fragmentManager?.beginTransaction() ?: return
            var wasContainerAttached = false
            containers.forEach { container ->
                if (container.getContainerName() == command.screen.screenKey) {
                    transaction.attach(container)
                    wasContainerAttached = true
                } else {
                    transaction.detach(container)
                }
            }
            if (!wasContainerAttached) {
                throw RuntimeException("Container = ${command.screen.screenKey} not found!")
            }
            transaction.commitNow()
        } else {
            super.applyCommand(command)
        }
    }

}