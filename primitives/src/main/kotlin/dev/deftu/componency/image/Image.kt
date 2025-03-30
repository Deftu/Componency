package dev.deftu.componency.image

import java.io.File
import java.io.InputStream
import java.net.URLConnection
import java.nio.file.Path
import kotlin.io.path.extension

public class Image(
    public val type: ImageType,
    public val data: ByteArray
) {

    public companion object {

        @JvmStatic
        public fun from(file: File): Image {
            return Image(ImageType.from(file.extension), file.readBytes())
        }

        @JvmStatic
        public fun from(path: Path): Image {
            return Image(ImageType.from(path.extension), path.toFile().readBytes())
        }

        @JvmStatic
        public fun from(inputStream: InputStream): Image? {
            val contentType = URLConnection.guessContentTypeFromStream(inputStream)
            if (contentType == null || !contentType.startsWith("image/")) {
                return null
            }

            val extension = contentType.substringAfter("image/")
            return Image(ImageType.from(extension), inputStream.readBytes())
        }

        @JvmStatic
        public fun from(data: ByteArray): Image? {
            return from(data.inputStream())
        }

    }

    public var width: Float = 0f
    public var height: Float = 0f

    public constructor(
        type: ImageType,
        inputStream: InputStream
    ) : this(
        type = type,
        data = inputStream.readBytes()
    )

    public constructor(
        type: ImageType,
        path: Path
    ) : this(
        type = type,
        data = path.toFile().readBytes()
    )

}
