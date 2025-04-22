package dev.deftu.componency.image

import kotlin.jvm.JvmField

public class Image(
    public val type: ImageType,
    public val data: ByteArray
) {

    public companion object {

        @JvmField
        public val EMPTY: Image = Image(ImageType.UNKNOWN, ByteArray(0))

    }

    public var width: Float = 0f
    public var height: Float = 0f

}
