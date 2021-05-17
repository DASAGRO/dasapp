package kz.das.dasaccounting.core.navigation.container
import androidx.collection.ArrayMap
import ru.terrakok.cicerone.Cicerone

object LocalCiceroneHolder {

    private val containers = ArrayMap<String, Cicerone<AppRouter>>()

    fun getCicerone(containerTag: String): Cicerone<AppRouter> {
        if (!containers.containsKey(containerTag)) {
            val appRouter = AppRouter()
            containers[containerTag] = Cicerone.create(appRouter)
        }
        return containers[containerTag]!!
    }

}