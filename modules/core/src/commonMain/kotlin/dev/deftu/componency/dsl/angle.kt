package dev.deftu.componency.dsl

import dev.deftu.componency.properties.impl.AngleDegreesProperty

public val Number.degrees: AngleDegreesProperty
    get() = AngleDegreesProperty(this.toFloat())

public val Number.degree: AngleDegreesProperty
    get() = AngleDegreesProperty(this.toFloat())

public val Number.deg: AngleDegreesProperty
    get() = AngleDegreesProperty(this.toFloat())

public fun Number.degrees(): AngleDegreesProperty {
    return AngleDegreesProperty(this.toFloat())
}

public fun Number.degree(): AngleDegreesProperty {
    return AngleDegreesProperty(this.toFloat())
}

public fun Number.deg(): AngleDegreesProperty {
    return AngleDegreesProperty(this.toFloat())
}
