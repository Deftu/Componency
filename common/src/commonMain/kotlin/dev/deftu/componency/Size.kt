package dev.deftu.componency

public data class Size(
    public val width: Float,
    public val height: Float
) {
    public companion object {
        public val Zero: Size = Size(0f, 0f)
    }

    public val area: Float get() = width * height
    public val isZero: Boolean get() = this == Zero

    public operator fun plus(other: Size): Size {
        return Size(width + other.width, height + other.height)
    }

    public operator fun minus(other: Size): Size {
        return Size(width - other.width, height - other.height)
    }

    public operator fun times(scalar: Float): Size {
        return Size(width * scalar, height * scalar)
    }

    public operator fun div(scalar: Float): Size {
        return Size(width / scalar, height / scalar)
    }

    override fun toString(): String {
        return "${width}x${height}"
    }
}
