package kz.das.dasaccounting.core.ui.utils.exceptions

open class BackendResponseException(
    val errorCode: Int,
    message: String
) : Exception(message)