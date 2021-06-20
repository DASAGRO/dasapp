package kz.das.dasaccounting.core.extensions

internal fun String.hexToString(): String {
    val stringBuilder = StringBuilder()
    val hexData = toCharArray()
    for (count in 0 until hexData.size step 2) {
        val firstDigit = Character.digit(hexData[count], 16)
        val lastDigit = Character.digit(hexData[count + 1], 16)
        val decimal = firstDigit * 16 + lastDigit
        stringBuilder.append(decimal.toChar())
    }
    return stringBuilder.toString()
}

fun String.compareStrAndInit(compareStr: String, action: () -> Unit): Boolean {
    if (this == compareStr) {
        action()
    }
    return this == compareStr
}