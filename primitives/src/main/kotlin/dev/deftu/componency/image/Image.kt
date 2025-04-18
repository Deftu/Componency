package dev.deftu.componency.image

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.net.URLConnection
import java.nio.file.Path
import javax.imageio.ImageIO
import kotlin.io.path.extension

public class Image(
    public val type: ImageType,
    public val data: ByteArray
) {

    public companion object {

        @JvmField
        public val EMPTY: Image = Image(ImageType.UNKNOWN, ByteArray(0))

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

        @JvmStatic
        public fun from(image: BufferedImage): Image {
            val output = ByteArrayOutputStream()
            ImageIO.write(image, "png", output)
            return Image(ImageType.RASTER, output.toByteArray()).apply {
                width = image.width.toFloat()
                height = image.height.toFloat()
            }
        }

        @JvmStatic
        public fun createBufferedImage(image: Image): BufferedImage {
            val inputStream = image.data.inputStream()
            return ImageIO.read(inputStream) ?: throw IllegalStateException("Failed to decode image data")
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
