package dev.deftu.componency.lwjgl3.engine.image

import dev.deftu.componency.image.Image
import dev.deftu.componency.image.ImageType
import dev.deftu.componency.platform.image.ImageLoader
import dev.deftu.componency.platform.image.JVMPlatformImage
import dev.deftu.componency.platform.image.PlatformImage
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

    override fun loadFromPlatform(image: PlatformImage): Image? {
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

    override fun convertToPlatform(image: Image): PlatformImage {
        val inputStream = image.data.inputStream()
        val bufferedImage = ImageIO.read(inputStream)
        return JVMPlatformImage(bufferedImage)
    }

}
