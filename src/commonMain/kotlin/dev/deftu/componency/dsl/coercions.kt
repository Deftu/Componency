package dev.deftu.componency.dsl

import dev.deftu.componency.properties.Property
import dev.deftu.componency.properties.impl.*

public infix fun Property<Float>.coerceAtLeast(min: Property<Float>): CoerceAtLeastProperty {
    return CoerceAtLeastProperty(this, min)
}

public infix fun Property<Float>.coerceAtMost(max: Property<Float>): CoerceAtMostProperty {
    return CoerceAtMostProperty(this, max)
}

public fun Property<Float>.coerceIn(min: Property<Float>, max: Property<Float>): CoerceInProperty {
    return CoerceInProperty(this, min, max)
}
