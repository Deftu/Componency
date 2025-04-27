package dev.deftu.componency

import java.io.InputStream

public class ByteInputStream(private val inputStream: InputStream) : ByteStream {

    override fun read(buffer: ByteArray, offset: Int, length: Int): Int {
        return inputStream.read(buffer, offset, length)
    }

    override fun skip(n: Int) {
        inputStream.skip(n.toLong())
    }

    override fun close() {
        inputStream.close()
    }
}
