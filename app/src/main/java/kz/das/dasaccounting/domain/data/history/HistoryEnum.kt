package kz.das.dasaccounting.domain.data.history

enum class HistoryEnum(val status: String) {
    SENT("Передан"),
    ACCEPTED("Принят"),
    AWAIT("В ожидании"),
    UNFINISHED("Неоконченный"),
}