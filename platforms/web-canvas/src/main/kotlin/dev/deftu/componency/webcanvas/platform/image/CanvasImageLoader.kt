package dev.deftu.componency.webcanvas.platform.image

import dev.deftu.componency.image.Image
import dev.deftu.componency.image.ImageType
import dev.deftu.componency.platform.image.ImageLoader
import dev.deftu.componency.platform.image.PlatformImage
import kotlinx.browser.document
import org.khronos.webgl.Uint8Array
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

public object CanvasImageLoader : ImageLoader {

    override fun loadFromData(data: ByteArray): Image? {
        val blob = Blob(arrayOf(Uint8Array(data.toTypedArray())), BlobPropertyBag("image/png"))
        val url = URL.createObjectURL(blob)

        val img = document.createElement("img") as HTMLImageElement
        img.src = url

        // Workaround for sync context: dimensions are only available *after* load
        // So we create the Image, then update width/height via JS event hook
        val image = Image(ImageType.RASTER, data)

        img.onload = {
            image.width = img.naturalWidth.toFloat()
            image.height = img.naturalHeight.toFloat()
            URL.revokeObjectURL(url) // Clean up the URL after loading
        }

        return image
    }

    override fun loadEncodedFromPlatform(image: PlatformImage): Image? {
        val platformImage = image as? CanvasPlatformImage ?: return null
        val img = platformImage.element

        // We don’t have access to the original ByteArray here, so return empty
        val result = Image(ImageType.UNKNOWN, ByteArray(0))
        result.width = img.naturalWidth.toFloat()
        result.height = img.naturalHeight.toFloat()
        return result
    }

    override fun convertEncodedToPlatform(image: Image): PlatformImage? {
        val blob = Blob(arrayOf(Uint8Array(image.data.toTypedArray())), BlobPropertyBag("image/png"))
        val url = URL.createObjectURL(blob)

        val img = document.createElement("img") as HTMLImageElement
        img.src = url

        return CanvasPlatformImage(img)
    }

    override fun loadRawFromPlatform(image: PlatformImage): Image? {
        // No pixel access without Canvas, so same as encoded
        return loadEncodedFromPlatform(image)
    }

    override fun convertRawToPlatform(image: Image): PlatformImage? {
        // Same logic again
        return convertEncodedToPlatform(image)
    }

}
