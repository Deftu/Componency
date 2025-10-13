package dev.deftu.componency

public data class AdditionDimension(private val first: Dimension, private val second: Dimension) : Dimension {
    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        return first.toPhysicalPixels(density, axisMaxPixels) + second.toPhysicalPixels(density, axisMaxPixels)
    }
}

public data class SubtractionDimension(private val first: Dimension, private val second: Dimension) : Dimension {
    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        return first.toPhysicalPixels(density, axisMaxPixels) - second.toPhysicalPixels(density, axisMaxPixels)
    }
}

public data class MultiplicationDimension(private val first: Dimension, private val second: Dimension) : Dimension {
    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        return first.toPhysicalPixels(density, axisMaxPixels) * second.toPhysicalPixels(density, axisMaxPixels)
    }
}

public data class DivisionDimension(private val first: Dimension, private val second: Dimension) : Dimension {
    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        return first.toPhysicalPixels(density, axisMaxPixels) / second.toPhysicalPixels(density, axisMaxPixels)
    }
}

public data class CoerceInDimension(
    private val dimension: Dimension,
    private val min: Dimension,
    private val max: Dimension
) : Dimension {
    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        val value = dimension.toPhysicalPixels(density, axisMaxPixels)
        return value.coerceIn(
            min.toPhysicalPixels(density, axisMaxPixels),
            max.toPhysicalPixels(density, axisMaxPixels)
        )
    }
}

public data class CoerceAtLeastDimension(
    private val dimension: Dimension,
    private val min: Dimension
) : Dimension {
    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        val value = dimension.toPhysicalPixels(density, axisMaxPixels)
        return value.coerceAtLeast(min.toPhysicalPixels(density, axisMaxPixels))
    }
}

public data class CoerceAtMostDimension(
    private val dimension: Dimension,
    private val max: Dimension
) : Dimension {
    override fun toPhysicalPixels(density: Density, axisMaxPixels: Float?): Float {
        val value = dimension.toPhysicalPixels(density, axisMaxPixels)
        return value.coerceAtMost(max.toPhysicalPixels(density, axisMaxPixels))
    }
}

public operator fun Dimension.plus(other: Dimension): Dimension {
    return AdditionDimension(this, other)
}

public operator fun Int.plus(other: Dimension): Dimension {
    return AdditionDimension(other, UnitDimension(this.toFloat()))
}

public operator fun Float.plus(other: Dimension): Dimension {
    return AdditionDimension(UnitDimension(this), other)
}

public operator fun Dimension.minus(other: Dimension): Dimension {
    return SubtractionDimension(this, other)
}

public operator fun Int.minus(other: Dimension): Dimension {
    return SubtractionDimension(UnitDimension(this.toFloat()), other)
}

public operator fun Float.minus(other: Dimension): Dimension {
    return SubtractionDimension(UnitDimension(this), other)
}

public operator fun Dimension.times(other: Dimension): Dimension {
    return MultiplicationDimension(this, other)
}

public operator fun Int.times(other: Dimension): Dimension {
    return MultiplicationDimension(UnitDimension(this.toFloat()), other)
}

public operator fun Float.times(other: Dimension): Dimension {
    return MultiplicationDimension(UnitDimension(this), other)
}

public operator fun Dimension.div(other: Dimension): Dimension {
    return DivisionDimension(this, other)
}

public operator fun Int.div(other: Dimension): Dimension {
    return DivisionDimension(UnitDimension(this.toFloat()), other)
}

public operator fun Float.div(other: Dimension): Dimension {
    return DivisionDimension(UnitDimension(this), other)
}

public fun Dimension.coerceIn(min: Dimension, max: Dimension): Dimension {
    return CoerceInDimension(this, min, max)
}

public fun Dimension.coerceAtLeast(min: Dimension): Dimension {
    return CoerceAtLeastDimension(this, min)
}

public fun Dimension.coerceAtMost(max: Dimension): Dimension {
    return CoerceAtMostDimension(this, max)
}
