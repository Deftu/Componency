package dev.deftu.componency.lwjgl3.engine.image

import dev.deftu.componency.image.Image
import dev.deftu.componency.image.ImageType
import dev.deftu.componency.platform.image.ImageLoader
import dev.deftu.componency.platform.image.JVMPlatformImage
import dev.deftu.componency.platform.image.PlatformImage
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.io.ByteArrayOutputStream
import java.net.URLConnection
import javax.imageio.ImageIO

class Lwjgl3ImageLoader : ImageLoader {

    override fun loadFromData(data: ByteArray): Image? {
        val inputStream = data.inputStream()
        val contentType = URLConnection.guessContentTypeFromStream(inputStream)
        if (contentType == null || !contentType.startsWith("image/")) {
            return null
        }

        val extension = contentType.substringAfter("image/")
        return Image(ImageType.from(extension), data)
    }

    override fun loadEncodedFromPlatform(image: PlatformImage): Image? {
        require(image is JVMPlatformImage)

        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image.value, "png", outputStream)
        val data = outputStream.toByteArray()
        val contentType = URLConnection.guessContentTypeFromStream(data.inputStream())
        if (contentType == null || !contentType.startsWith("image/")) {
            return null
        }

        val extension = contentType.substringAfter("image/")
        return Image(ImageType.from(extension), data)
    }

    override fun convertEncodedToPlatform(image: Image): PlatformImage {
        val inputStream = image.data.inputStream()
        val bufferedImage = ImageIO.read(inputStream)
        return JVMPlatformImage(bufferedImage)
    }

    override fun loadRawFromPlatform(image: PlatformImage): Image? {
        val bufferedImage = (image as? JVMPlatformImage)?.value ?: return null
        val width = bufferedImage.width
        val height = bufferedImage.height

        val pixels = IntArray(width * height)
        bufferedImage.getRGB(0, 0, width, height, pixels, 0, width)

        val rgbaBytes = ByteArray(width * height * 4)

        for (i in pixels.indices) {
            val pixel = pixels[i]
            val offset = i * 4
            rgbaBytes[offset + 0] = ((pixel shr 16) and 0xFF).toByte() // R
            rgbaBytes[offset + 1] = ((pixel shr 8) and 0xFF).toByte()  // G
            rgbaBytes[offset + 2] = (pixel and 0xFF).toByte()          // B
            rgbaBytes[offset + 3] = ((pixel shr 24) and 0xFF).toByte() // A
        }

        return Image(
            type = ImageType.RASTER,
            data = rgbaBytes
        ).apply {
            this.width = width.toFloat()
            this.height = height.toFloat()
        }
    }

    override fun convertRawToPlatform(image: Image): PlatformImage? {
        val width = image.width.toInt()
        val height = image.height.toInt()

        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val pixels = (bufferedImage.raster.dataBuffer as DataBufferInt).data

        for (i in 0 until width * height) {
            val offset = i * 4
            val r = (image.data[offset + 0].toInt() and 0xFF)
            val g = (image.data[offset + 1].toInt() and 0xFF)
            val b = (image.data[offset + 2].toInt() and 0xFF)
            val a = (image.data[offset + 3].toInt() and 0xFF)

            pixels[i] = (a shl 24) or (r shl 16) or (g shl 8) or b
        }

        return JVMPlatformImage(bufferedImage)
    }

}
