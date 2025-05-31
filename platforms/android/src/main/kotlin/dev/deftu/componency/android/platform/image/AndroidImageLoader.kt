package dev.deftu.componency.android.platform.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dev.deftu.componency.image.Image
import dev.deftu.componency.image.ImageType
import dev.deftu.componency.platform.image.ImageLoader
import dev.deftu.componency.platform.image.PlatformImage
import java.io.ByteArrayOutputStream

public object AndroidImageLoader : ImageLoader {

    override fun loadFromData(data: ByteArray): Image? {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size) ?: return null
        val type = guessImageType(data)
        val image = Image(type, data)
        image.width = bitmap.width.toFloat()
        image.height = bitmap.height.toFloat()
        return image
    }

    override fun loadEncodedFromPlatform(image: PlatformImage): Image? {
        if (image !is AndroidPlatformImage) return null
        val outputStream = ByteArrayOutputStream()
        image.value.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val bytes = outputStream.toByteArray()
        val type = guessImageType(bytes)
        val result = Image(type, bytes)
        result.width = image.value.width.toFloat()
        result.height = image.value.height.toFloat()
        return result
    }

    override fun convertEncodedToPlatform(image: Image): PlatformImage? {
        val bitmap = BitmapFactory.decodeByteArray(image.data, 0, image.data.size) ?: return null
        return AndroidPlatformImage(bitmap)
    }

    override fun loadRawFromPlatform(image: PlatformImage): Image? {
        return loadEncodedFromPlatform(image)
    }

    override fun convertRawToPlatform(image: Image): PlatformImage? {
        return convertEncodedToPlatform(image)
    }

    private fun guessImageType(bytes: ByteArray): ImageType {
        if (bytes.size >= 8) {
            if (bytes[0] == 0x89.toByte() &&
                bytes[1] == 0x50.toByte() && // P
                bytes[2] == 0x4E.toByte() && // N
                bytes[3] == 0x47.toByte() && // G
                bytes[4] == 0x0D.toByte() &&
                bytes[5] == 0x0A.toByte() &&
                bytes[6] == 0x1A.toByte() &&
                bytes[7] == 0x0A.toByte()
            ) {
                return ImageType.RASTER // PNG
            }
        }
        if (bytes.size >= 2) {
            if (bytes[0] == 0xFF.toByte() && bytes[1] == 0xD8.toByte()) {
                return ImageType.RASTER // JPEG
            }
            if (bytes[0] == 0x42.toByte() && bytes[1] == 0x4D.toByte()) {
                return ImageType.RASTER // BMP
            }
        }
        if (bytes.size >= 4) {
            val str = String(bytes, 0, 4)
            if (str.startsWith("<svg", ignoreCase = true)) {
                return ImageType.VECTOR // SVG
            }
        }
        return ImageType.UNKNOWN
    }

}
