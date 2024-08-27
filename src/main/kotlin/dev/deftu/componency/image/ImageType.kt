package dev.deftu.componency.image

public enum class ImageType(
    public val extensions: Set<String>
) {
    RASTER("bmp", "png", "jpg", "jpeg", "jpe", "jif", "jfif", "jfi"),
    VECTOR("svg"),
    UNKNOWN;

    constructor(vararg extensions: String) : this(extensions.toSet())

    public companion object {

        public fun from(value: String): ImageType {
            for (type in values()) {
                if (type.extensions.contains(value)) {
                    return type
                }
            }

            return UNKNOWN
        }

    }

}
