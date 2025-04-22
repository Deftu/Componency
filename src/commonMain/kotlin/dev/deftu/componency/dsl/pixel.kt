package dev.deftu.componency.dsl

import dev.deftu.componency.properties.impl.PixelProperty

public val Number.px: PixelProperty
    get() = PixelProperty(this.toFloat())

public fun Number.px(): PixelProperty {
    return PixelProperty(this.toFloat())
}

public fun Number.px(isInverse: Boolean): PixelProperty {
    return PixelProperty(this.toFloat(), isInverse)
}
