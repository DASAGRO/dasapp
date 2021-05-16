package kz.das.dasaccounting.core.navigation

import ru.terrakok.cicerone.Router

interface RouterProvider {
    fun getRouter(): Router
}