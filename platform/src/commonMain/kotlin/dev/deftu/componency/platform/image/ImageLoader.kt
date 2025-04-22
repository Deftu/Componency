package dev.deftu.componency.platform.image

import dev.deftu.componency.image.Image

public interface ImageLoader {

    public fun loadFromData(data: ByteArray): Image?

    public fun loadFromPlatform(image: PlatformImage): Image?

    public fun convertToPlatform(image: Image): PlatformImage?

}
