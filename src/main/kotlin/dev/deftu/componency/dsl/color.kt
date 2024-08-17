package dev.deftu.componency.dsl

import dev.deftu.componency.color.Color
import dev.deftu.componency.properties.impl.StaticColorProperty

public val Color.asProperty: StaticColorProperty
    get() = StaticColorProperty(this)

public fun Color.asProperty(): StaticColorProperty {
    return StaticColorProperty(this)
}

public fun Color.withAlpha(alpha: Int): Color {
    return Color.rgba(red, green, blue, alpha)
}

public fun Color.withAlphaPercentage(percent: Float): Color {
    val alpha = (percent * 255).toInt()
    return Color.rgba(red, green, blue, alpha)
}
