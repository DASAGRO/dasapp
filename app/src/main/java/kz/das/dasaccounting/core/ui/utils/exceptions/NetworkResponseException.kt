package kz.das.dasaccounting.core.ui.utils.exceptions

class NetworkResponseException(
        message: String,
        val httpResponseCode: Int
) : Exception(message)