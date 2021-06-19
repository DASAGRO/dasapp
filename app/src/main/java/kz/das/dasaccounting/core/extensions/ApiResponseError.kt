package kz.das.dasaccounting.core.extensions

class ApiError(var title: String = "Ошибка",
               var descr: String = "Описание ошибка")

fun String?.toStringListApiError(): ApiError {
    return if (!this.isNullOrEmpty()) {
        var list = this
        list = list.replace("[", "")
        list = list.replace("]", "")
        list = list.replace("\"", "")
        val result: List<String> = list.split(",").map {
            it.trim()
        }
        if (result.size > 1)
            ApiError(result[0], result[1])
        else
            ApiError("Неожиданная ошибка", result[0])
    } else {
        ApiError("Неожиданная ошибка", "Что-то пошло не так")
    }
}
