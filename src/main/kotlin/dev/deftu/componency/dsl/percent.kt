package dev.deftu.componency.dsl

import dev.deftu.componency.properties.impl.ParentRelativeProperty
import dev.deftu.componency.properties.impl.RootRelativeProperty

public val Number.percent: ParentRelativeProperty
    get() = ParentRelativeProperty(this.toFloat() / 100f)

public fun Number.percent(): ParentRelativeProperty {
    return ParentRelativeProperty(this.toFloat() / 100f)
}

public val Number.percentOfRoot: RootRelativeProperty
    get() = RootRelativeProperty(this.toFloat() / 100f)

public fun Number.percentOfRoot(): RootRelativeProperty {
    return RootRelativeProperty(this.toFloat() / 100f)
}
