package dev.deftu.componency

public data class Bounds(
    public val coordinates: Coordinates,
    public val size: Size
) {
    public companion object {
        public val Zero: Bounds = Bounds(Coordinates.Zero, Size.Zero)

        public fun xywh(x: Float, y: Float, width: Float, height: Float): Bounds {
            return Bounds(Coordinates(x, y), Size(width, height))
        }

        public fun ltrb(left: Float, top: Float, right: Float, bottom: Float): Bounds {
            return xywh(left, top, right - left, bottom - top)
        }
    }

    public val left: Float get() = coordinates.x
    public val top: Float get() = coordinates.y
    public val right: Float get() = coordinates.x + size.width
    public val bottom: Float get() = coordinates.y + size.height

    public val centerX: Float get() = coordinates.x + size.width / 2
    public val centerY: Float get() = coordinates.y + size.height / 2
    public val center: Coordinates get() = Coordinates(centerX, centerY)

    public fun contains(point: Coordinates): Boolean {
        return point.x in left..right && point.y in top..bottom
    }

    public fun intersects(other: Bounds): Boolean {
        return left < other.right && right > other.left &&
               top < other.bottom && bottom > other.top
    }
}
