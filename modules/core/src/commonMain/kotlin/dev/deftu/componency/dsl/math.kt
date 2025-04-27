package dev.deftu.componency.dsl

import dev.deftu.componency.properties.Property
import dev.deftu.componency.properties.impl.AdditiveProperty
import dev.deftu.componency.properties.impl.ScalingProperty
import dev.deftu.componency.properties.impl.SubtractiveProperty

public operator fun Property<Float>.plus(other: Property<Float>): AdditiveProperty {
    return AdditiveProperty(this, other)
}

public operator fun Property<Float>.minus(other: Property<Float>): SubtractiveProperty {
    return SubtractiveProperty(this, other)
}

public operator fun Property<Float>.times(factor: Number): ScalingProperty {
    return ScalingProperty(this, factor.toFloat())
}

public operator fun Property<Float>.div(divisor: Number): ScalingProperty {
    return ScalingProperty(this, 1f / divisor.toFloat())
}
