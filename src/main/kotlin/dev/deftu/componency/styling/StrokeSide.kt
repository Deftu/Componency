package dev.deftu.componency.styling

public enum class StrokeSide {

    TOP,
    RIGHT,
    BOTTOM,
    LEFT;

    public companion object {

        @JvmField
        public val ALL: Set<StrokeSide> = setOf(TOP, RIGHT, BOTTOM, LEFT)

    }

}
