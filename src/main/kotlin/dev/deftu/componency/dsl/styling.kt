package dev.deftu.componency.dsl

import dev.deftu.componency.color.Color
import dev.deftu.componency.properties.impl.StaticColorProperty
import dev.deftu.componency.properties.impl.StaticStrokeProperty
import dev.deftu.componency.stroke.Stroke

public val Color.asProperty: StaticColorProperty
    get() = StaticColorProperty(this)

public val Stroke.asProperty: StaticStrokeProperty
    get() = StaticStrokeProperty(this)

public fun Color.asProperty(): StaticColorProperty {
    return StaticColorProperty(this)
}

public fun Stroke.asProperty(): StaticStrokeProperty {
    return StaticStrokeProperty(this)
}
