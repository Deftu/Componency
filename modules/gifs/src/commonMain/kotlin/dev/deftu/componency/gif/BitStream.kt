package dev.deftu.componency.gif

public class BitStream(private val data: ByteArray) {

    private var bytePos = 0
    private var bitPos = 0

    public fun readBits(count: Int): Int {
        var result = 0
        for (i in 0 until count) {
            if (bytePos >= data.size) throw IllegalStateException("Unexpected EOF in bitstream")
            val bit = (data[bytePos].toInt() shr bitPos) and 1
            result = result or (bit shl i)
            bitPos++
            if (bitPos == 8) {
                bitPos = 0
                bytePos++
            }
        }

        return result
    }

}
