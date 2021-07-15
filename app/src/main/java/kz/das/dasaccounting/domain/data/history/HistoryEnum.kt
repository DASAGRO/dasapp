package kz.das.dasaccounting.domain.data.history

enum class HistoryEnum(val status: String) {
    SENT("Отправлен"),
    ACCEPTED("Получен"),
    AWAIT("В ожидании")
}