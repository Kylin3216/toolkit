package tech.dreamfund

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import tech.dreamfund.math.toHexString
import tech.dreamfund.math.toU32Bytes
import tech.dreamfund.str.hexToByteArray
import tech.dreamfund.str.toHexString
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis

@ObsoleteCoroutinesApi
fun main(args: Array<String>) {
    //print(FaceData(ByteArray(1),ByteArray(2)).parseHead())
    print(24.toString(2))
}

suspend fun test(data: (a: Int, b: Int) -> Unit) {
    withContext(Dispatchers.Default) {
        launch {
            val a = async {
                delay(3000)
                1
            }
            val b = async {
                delay(2000)
                2
            }
            data(a.await(), b.await())
        }
    }
}

class FaceData(
    val photo: ByteArray? = null,
    val feature: ByteArray? = null,
    val key: Int? = null,
    val score: Int? = null,
    val success: Boolean? = null
) {
    fun parseHead(): Byte {
        var head = ""
        head += if (photo == null) "0" else "1"
        head += if (feature == null) "0" else "1"
        head += if (key == null) "0" else "1"
        head += if (score == null) "0" else "1"
        head += if (success == null) "0" else "1"
        return head.toByte(2)
    }
}