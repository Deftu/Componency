package dev.deftu.componency

import kotlin.jvm.JvmInline

public sealed interface Dimension {
    public fun toPhysicalPixels(density: Density, axisMaxPixels: Float? = null): Float
    override fun toString(): String
}

@JvmInline
public value class UnitDimension(private val value: Float) : Dimension {
    public companion object {
        public val Zero: UnitDimension = UnitDimension(0f)
    }

    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        return value * density.pxPerDp
    }

    override fun toString(): String {
        return "Unit(${value}dp)"
    }
}

@JvmInline
public value class PixelDimension(private val value: Float) : Dimension {
    public companion object {
        public val Zero: PixelDimension = PixelDimension(0f)
    }

    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        return value
    }

    override fun toString(): String {
        return "Pixel(${value}px)"
    }
}

@JvmInline
public value class TextDimension(private val value: Float) : Dimension {
    public companion object {
        public val Zero: TextDimension = TextDimension(0f)
    }

    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        return value * density.fontScale
    }

    override fun toString(): String {
        return "Text(${value}sp)"
    }
}

@JvmInline
public value class ScaledDimension(private val value: Float) : Dimension {
    public companion object {
        public val Zero: ScaledDimension = ScaledDimension(0f)
    }

    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        val max = axisMaxPixels ?: Float.POSITIVE_INFINITY
        return (value * max).coerceAtMost(max)
    }

    override fun toString(): String {
        return "Scaled(${value * 100}%)"
    }
}

public inline val Int.unit: UnitDimension get() = UnitDimension(this.toFloat())
public inline val Float.unit: UnitDimension get() = UnitDimension(this)

public inline val Int.px: PixelDimension get() = PixelDimension(this.toFloat())
public inline val Float.px: PixelDimension get() = PixelDimension(this)

public inline val Int.sp: TextDimension get() = TextDimension(this.toFloat())
public inline val Float.sp: TextDimension get() = TextDimension(this)

public inline val Int.percent: ScaledDimension get() = ScaledDimension(this / 100f)
public inline val Float.percent: ScaledDimension get() = ScaledDimension(this)
