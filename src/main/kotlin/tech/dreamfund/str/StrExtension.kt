package tech.dreamfund.str

fun ByteArray.toHexString(split: String = ""): String {
    var cmdStr = ""
    for (i in this.indices) {
        cmdStr += String.format("%02x", this[i]).toUpperCase() + split
    }
    return cmdStr
}

private const val HEX_CHARS = "0123456789ABCDEF"

fun String.hexToByteArray(): ByteArray {

    val result = ByteArray(length / 2)

    for (i in 0 until length step 2) {
        val firstIndex = HEX_CHARS.indexOf(this[i], ignoreCase = true)
        val secondIndex = HEX_CHARS.indexOf(this[i + 1], ignoreCase = true)
        val octet = firstIndex.shl(4).or(secondIndex)
        result[i.shr(1)] = octet.toByte()
    }

    return result
}