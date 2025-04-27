@file:JvmName("ByteStreamHelper")

package dev.deftu.componency

import java.io.File
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.inputStream

@JvmName("createByteStream")
public fun InputStream.toByteStream(): ByteStream {
    return ByteInputStream(this)
}

@JvmName("createByteStream")
public fun Path.toByteStream(): ByteStream {
    return inputStream().toByteStream()
}

@JvmName("createByteStream")
public fun File.toByteStream(): ByteStream {
    return inputStream().toByteStream()
}
