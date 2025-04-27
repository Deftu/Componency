package dev.deftu.componency.gif

internal object LzwDecoder {

    fun decode(width: Int, height: Int, minCodeSize: Int, data: ByteArray): IntArray {
        val stream = BitStream(data)
        val clearCode = 1 shl minCodeSize
        val endOfInformation = clearCode + 1
        var codeSize = minCodeSize + 1
        var dictSize = endOfInformation + 1

        val dictionary = mutableMapOf<Int, List<Int>>()
        for (i in 0 until clearCode) {
            dictionary[i] = listOf(i)
        }

        val pixels = mutableListOf<Int>()

        var prevCode: Int? = null

        while (true) {
            val code = stream.readBits(codeSize)
            if (code == clearCode) {
                dictionary.clear()
                for (i in 0 until clearCode) {
                    dictionary[i] = listOf(i)
                }
                dictSize = endOfInformation + 1
                codeSize = minCodeSize + 1
                prevCode = null
                continue
            }

            if (code == endOfInformation) {
                break
            }

            val entry = when {
                dictionary.containsKey(code) -> dictionary[code]!!
                code == dictSize && prevCode != null -> dictionary[prevCode]!! + dictionary[prevCode]!!.first()
                else -> throw IllegalStateException("Invalid LZW code")
            }

            pixels.addAll(entry)
            if (prevCode != null) {
                dictionary[dictSize++] = dictionary[prevCode]!! + entry.first()
                if (dictSize == (1 shl codeSize) && codeSize < 12) {
                    codeSize++
                }
            }

            prevCode = code
        }

        return pixels.take(width * height).toIntArray()
    }

}
