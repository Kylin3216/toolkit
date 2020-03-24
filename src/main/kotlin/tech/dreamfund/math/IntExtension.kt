package tech.dreamfund.math


fun Int.toU16Bytes(): ByteArray {
    val bytes = ByteArray(2)
    bytes[1] = (this and 0xFF).toByte()
    bytes[0] = (this shr 8 and 0xFF).toByte()
    return bytes
}

fun Int.toU32Bytes(): ByteArray {
    val bytes = ByteArray(4)
    bytes[3] = (this and 0xFF).toByte()
    bytes[2] = (this shr 8 and 0xFF).toByte()
    bytes[1] = (this shr 16 and 0xFF).toByte()
    bytes[0] = (this shr 24 and 0xFF).toByte()
    return bytes
}
