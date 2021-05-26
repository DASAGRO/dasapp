package kz.das.dasaccounting.core.ui.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.TextView
import kz.das.dasaccounting.R
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


fun TextView.setPhoneFormatter(): FormatWatcher {
    val mask = MaskImpl.createTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
    mask.isHideHardcodedHead = false
    val formatWatcher: FormatWatcher = MaskFormatWatcher(mask)
    formatWatcher.installOn(this)
    return formatWatcher
}

fun String.toNetworkFormattedPhone(): String {
    var newStr = this
    if (this.startsWith("8")) {
        newStr = newStr.replaceFirst("8", "7")
    } else if (this.startsWith("+8")) {
        newStr = newStr.replaceFirst("+8", "7")
    }
    newStr = newStr.replace(Regex("[\\(\\)\\- \\.]"), "")
    if (newStr.length <= 10) {
        newStr = "7$newStr"
    }
    newStr = newStr.replace("+", "")
    return newStr
}

fun CharSequence?.isValidPhoneNumber(): Boolean {
    if(this == null) {
        return false
    }
    return isNotBlank() && Patterns.PHONE.matcher(this).matches()
}

fun String.toUIFormattedPhone(): String {
    val inputMask = MaskImpl.createNonTerminated(PredefinedSlots.RUS_PHONE_NUMBER)
    inputMask.insertFront(this)
    return inputMask.toString()
}

fun String?.phoneCall(context: Context) {
    if (this.isNullOrEmpty() || this == context.getString(R.string.not_specified)) {
        return
    }
    val intent = Intent()
    intent.action = Intent.ACTION_DIAL
    intent.data = Uri.parse("tel:$this")
    context.startActivity(intent)
}