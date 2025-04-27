package dev.deftu.componency

public interface ByteStream : AutoCloseable {

    /**
     * Reads [length] bytes into the destination buffer starting at [offset].
     * Returns the number of bytes read, or -1 if EOF is reached.
     */
    public fun read(buffer: ByteArray, offset: Int = 0, length: Int = buffer.size): Int

    /**
     * Skips [n] bytes.
     */
    public fun skip(n: Int)

    public fun readFully(buffer: ByteArray) {
        var offset = 0
        while (offset < buffer.size) {
            val readBytes = read(buffer, offset, buffer.size - offset)
            if (readBytes == -1) {
                throw IllegalStateException("Unexpected EOF")
            }
            offset += readBytes
        }
    }

    /**
     * Reads exactly [n] bytes into a new array.
     */
    public fun readBytes(n: Int): ByteArray {
        val buffer = ByteArray(n)
        readFully(buffer)
        return buffer
    }

    /**
     * Reads 1 byte.
     */
    public fun readByte(): Byte {
        val single = ByteArray(1)
        val read = read(single, 0, 1)
        if (read == -1) throw IllegalStateException("EOF")
        return single[0]
    }

    /**
     * Reads 2 bytes as little-endian Int.
     */
    public fun readUShortLE(): Int {
        val b1 = readByte().toInt() and 0xFF
        val b2 = readByte().toInt() and 0xFF
        return (b2 shl 8) or b1
    }
}
