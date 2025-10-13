package dev.deftu.componency.canvas

public enum class Sampling {
    /**
     * Linear sampling, which is the default.
     */
    LINEAR,

    /**
     * Nearest neighbor sampling, which is faster but may produce pixelated results.
     */
    NEAREST,

    /**
     * Bilinear sampling, which provides smoother results than nearest neighbor.
     */
    BILINEAR,

    /**
     * Bicubic sampling, which provides even smoother results than bilinear.
     */
    BICUBIC,
}
