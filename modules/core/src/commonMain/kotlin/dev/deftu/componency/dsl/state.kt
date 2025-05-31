package dev.deftu.componency.dsl

import dev.deftu.componency.properties.ColorProperty
import dev.deftu.componency.properties.Property
import dev.deftu.componency.properties.impl.StaticColorProperty
import dev.deftu.kolor.Color
import dev.deftu.stateful.State

public val State<Color>.asProperty: ColorProperty
    get() = StaticColorProperty(this)

public fun State<Color>.asProperty(): ColorProperty {
    return StaticColorProperty(this)
}
