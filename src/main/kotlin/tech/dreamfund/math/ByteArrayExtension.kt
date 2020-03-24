package tech.dreamfund.math


fun ByteArray.toU16(): Int {
    if (this.size != 2) throw Throwable("字节数组长度必须为2")
    val b0 = this[0].toInt() and 0xFF
    val b1 = this[1].toInt() and 0xFF
    return (b0 shl 8) or b1
}

fun ByteArray.toU32(): Int {
    if (this.size != 4) throw Throwable("字节数组长度必须为4")
    val b0 = this[0].toInt() and 0xFF
    val b1 = this[1].toInt() and 0xFF
    val b2 = this[2].toInt() and 0xFF
    val b3 = this[3].toInt() and 0xFF
    return (b0 shl 24) or (b1 shl 16) or (b2 shl 8) or b3
}
