package dev.deftu.componency.dsl

import dev.deftu.componency.properties.impl.ParentRelativeProperty

public val Number.percent: ParentRelativeProperty
    get() = ParentRelativeProperty(this)

public fun Number.percent(): ParentRelativeProperty {
    return ParentRelativeProperty(this)
}
