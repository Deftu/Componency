package dev.deftu.componency

public data class Coordinates(
    public val x: Float,
    public val y: Float,
) {
    public companion object {
        public val Zero: Coordinates = Coordinates(0f, 0f)
    }

    public val isZero: Boolean get() = this == Zero

    public operator fun plus(other: Coordinates): Coordinates {
        return Coordinates(x = this.x + other.x, y = this.y + other.y)
    }

    public operator fun minus(other: Coordinates): Coordinates {
        return Coordinates(x = this.x - other.x, y = this.y - other.y)
    }

    public operator fun times(scalar: Float): Coordinates {
        return Coordinates(x = this.x * scalar, y = this.y * scalar)
    }

    public operator fun div(scalar: Float): Coordinates {
        return Coordinates(x = this.x / scalar, y = this.y / scalar)
    }

    override fun toString(): String {
        return "($x, $y)"
    }
}
