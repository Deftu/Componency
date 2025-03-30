package dev.deftu.componency.dsl

import dev.deftu.componency.animations.AnimationTime
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.properties.impl.*

public val ComponentProperties.centered: CenteredProperty
    get() = CenteredProperty()

public val ComponentProperties.hugging: ContentHuggingProperty
    get() = ContentHuggingProperty()

public val ComponentProperties.fill: FillProperty
    get() = FillProperty()

public val ComponentProperties.largestChild: LargestChildProperty
    get() = LargestChildProperty()

public val ComponentProperties.mousePosition: MousePositionProperty
    get() = MousePositionProperty()

public val ComponentProperties.rainbow: RainbowColorProperty
    get() = RainbowColorProperty()

public val ComponentProperties.siblingBased: SiblingBasedProperty
    get() = SiblingBasedProperty()

public fun ComponentProperties.centered(): CenteredProperty {
    return CenteredProperty()
}

public fun ComponentProperties.hugging(padding: Float = 0f): ContentHuggingProperty {
    return ContentHuggingProperty(padding)
}

public fun ComponentProperties.fill(useSiblings: Boolean = true): FillProperty {
    return FillProperty(useSiblings)
}

public fun ComponentProperties.largestChild(): LargestChildProperty {
    return LargestChildProperty()
}

public fun ComponentProperties.mousePosition(centerAligned: Boolean = false): MousePositionProperty {
    return MousePositionProperty(centerAligned)
}

public fun ComponentProperties.rainbow(
    cycleDuration: AnimationTime = 1.second,
    saturation: Float = 1f,
    brightness: Float = 1f
): RainbowColorProperty {
    return RainbowColorProperty(cycleDuration, saturation, brightness)
}

public fun ComponentProperties.siblingBased(
    padding: Float = 0f,
    isInverse: Boolean = false
): SiblingBasedProperty {
    return SiblingBasedProperty(padding, isInverse)
}
