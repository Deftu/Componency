package dev.deftu.componency.platform.audio

public interface AudioByteStream : AutoCloseable {

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

}
