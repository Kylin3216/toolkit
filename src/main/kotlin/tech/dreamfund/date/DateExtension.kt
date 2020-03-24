package tech.dreamfund.date

import java.text.SimpleDateFormat
import java.util.*

fun Date?.asString(pattern:String="yyyy-MM-dd HH:mm:ss"): String? {
    return if (this == null) null
    else SimpleDateFormat(pattern, Locale.CHINESE).format(this)
}