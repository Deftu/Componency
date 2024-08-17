package dev.deftu.componency.dsl

import dev.deftu.componency.properties.impl.StaticStrokeProperty
import dev.deftu.componency.styling.Stroke

public val Stroke.asProperty: StaticStrokeProperty
    get() = StaticStrokeProperty(this)

public fun Stroke.asProperty(): StaticStrokeProperty {
    return StaticStrokeProperty(this)
}
