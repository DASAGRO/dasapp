package kz.das.dasaccounting.domain.data.office

data class QrSession(
        val id: Int,
        val uuid: String,
        val name: String? = null,
        val comment: String? = null
)