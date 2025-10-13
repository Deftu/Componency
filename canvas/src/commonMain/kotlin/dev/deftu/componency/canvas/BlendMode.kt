package dev.deftu.componency.canvas

public enum class BlendMode {
    /**
     * The source color is drawn over the destination color.
     */
    SRC_OVER,

    /**
     * The source color replaces the destination color.
     */
    SRC,

    /**
     * The destination color is drawn over the source color.
     */
    DST_OVER,

    /**
     * The destination color is replaced by the source color.
     */
    DST,

    /**
     * The source and destination colors are combined using a multiply operation.
     */
    MULTIPLY,

    /**
     * The source and destination colors are combined using a screen operation.
     */
    SCREEN,

    /**
     * The source and destination colors are combined using an overlay operation.
     */
    OVERLAY,
}
