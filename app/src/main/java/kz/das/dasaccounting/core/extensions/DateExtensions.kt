package kz.das.dasaccounting.core.extensions

import java.text.SimpleDateFormat
import java.util.*


//01.01.2021 00:00:00
fun String?.getLongFromServerDate(): Long {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    return sdf.parse(this ?: "01.01.2021 00:00:00")?.time ?: System.currentTimeMillis()
}


//01.01.2021 00:00:00
fun Long?.getServerDateFromLong(): String  {
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(this ?: System.currentTimeMillis())) ?: sdf.format(Calendar.getInstance().time)
}
