package dev.deftu.componency.platform.image

import dev.deftu.componency.image.Image

public interface ImageLoader {

    public fun loadFromData(data: ByteArray): Image?

    public fun loadEncodedFromPlatform(image: PlatformImage): Image?

    public fun convertEncodedToPlatform(image: Image): PlatformImage?

    public fun loadRawFromPlatform(image: PlatformImage): Image?

    public fun convertRawToPlatform(image: Image): PlatformImage?

}
