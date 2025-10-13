package dev.deftu.componency.canvas

public enum class TileMode {
    /**
     * The source image is clamped to the edges of the destination rectangle.
     * Pixels outside the source image are not drawn.
     */
    CLAMP,

    /**
     * The source image is repeated in both directions to fill the destination rectangle.
     */
    REPEAT,

    /**
     * The source image is mirrored in both directions to fill the destination rectangle.
     */
    MIRROR,

    /**
     * The source image is repeated in both directions, but only the part that fits within the destination rectangle is drawn.
     */
    DECAL,
}
