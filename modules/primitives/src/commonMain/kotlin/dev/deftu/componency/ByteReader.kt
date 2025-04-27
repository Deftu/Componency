package dev.deftu.componency

public class ByteReader(private val data: ByteArray) {

    private var pos = 0

    public fun readByte(): Byte {
        return data[pos++]
    }

    public fun readShort(): Int {
        return (readByte().toInt() and 0xFF) or ((readByte().toInt() and 0xFF) shl 8)
    }

    public fun readBytes(count: Int): ByteArray {
        return ByteArray(count) {
            readByte()
        }
    }

    public fun expectString(expected: String) {
        val actual = readBytes(expected.length).toString()
        if (actual != expected) {
            throw IllegalArgumentException("Expected $expected, got $actual")
        }
    }

    public fun skip(count: Int) {
        pos += count
    }

    public fun skipSubBlocks() {
        var size = readByte().toInt() and 0xFF
        while (size != 0) {
            skip(size)
            size = readByte().toInt() and 0xFF
        }
    }

    public fun readSubBlocks(): ByteArray {
        val output = mutableListOf<Byte>()
        var size = readByte().toInt() and 0xFF
        while (size != 0) {
            output.addAll(readBytes(size).toList())
            size = readByte().toInt() and 0xFF
        }

        return output.toByteArray()
    }

    public fun readColorTable(size: Int): Array<Int> {
        return Array(size) {
            val r = readByte().toInt() and 0xFF
            val g = readByte().toInt() and 0xFF
            val b = readByte().toInt() and 0xFF
            (0xFF shl 24) or (r shl 16) or (g shl 8) or b
        }
    }

    public fun eof(): Boolean {
        return pos >= data.size
    }

}
