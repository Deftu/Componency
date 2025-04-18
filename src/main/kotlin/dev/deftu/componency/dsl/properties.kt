@file:Suppress("UnusedReceiverParameter")

package dev.deftu.componency.dsl

import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.properties.*
import dev.deftu.componency.properties.impl.*
import dev.deftu.componency.time.AnimationTime

public val ComponentProperties<*, *>.centered: CenteredProperty
    get() = CenteredProperty()

public val ComponentProperties<*, *>.hugging: ContentHuggingProperty
    get() = ContentHuggingProperty()

public val ComponentProperties<*, *>.filling: FillProperty
    get() = FillProperty()

public val ComponentProperties<*, *>.largestChild: LargestChildProperty
    get() = LargestChildProperty()

public val ComponentProperties<*, *>.pointerPosition: PointerPositionProperty
    get() = PointerPositionProperty()

public val ComponentProperties<*, *>.rainbow: RainbowColorProperty
    get() = RainbowColorProperty()

public val ComponentProperties<*, *>.siblingBased: SiblingBasedProperty
    get() = SiblingBasedProperty()

public fun ComponentProperties<*, *>.centered(): CenteredProperty {
    return CenteredProperty()
}

public fun ComponentProperties<*, *>.hugging(padding: Float = 0f): ContentHuggingProperty {
    return ContentHuggingProperty(padding)
}

public fun ComponentProperties<*, *>.filling(useSiblings: Boolean = true): FillProperty {
    return FillProperty(useSiblings)
}

public fun ComponentProperties<*, *>.linked(target: XProperty): LinkedProperty {
    return LinkedProperty(target as VectorProperty)
}

public fun ComponentProperties<*, *>.linked(target: YProperty): LinkedProperty {
    return LinkedProperty(target as VectorProperty)
}

public fun ComponentProperties<*, *>.linked(target: WidthProperty): LinkedProperty {
    return LinkedProperty(target as VectorProperty)
}

public fun ComponentProperties<*, *>.linked(target: HeightProperty): LinkedProperty {
    return LinkedProperty(target as VectorProperty)
}

public fun ComponentProperties<*, *>.linked(target: RadialProperty): LinkedProperty {
    return LinkedProperty(target as VectorProperty)
}

public fun ComponentProperties<*, *>.largestChild(): LargestChildProperty {
    return LargestChildProperty()
}

public fun ComponentProperties<*, *>.pointerPosition(centerAligned: Boolean = false): PointerPositionProperty {
    return PointerPositionProperty(centerAligned)
}

public fun ComponentProperties<*, *>.rainbow(
    cycleDuration: AnimationTime = 1.second,
    saturation: Float = 1f,
    brightness: Float = 1f
): RainbowColorProperty {
    return RainbowColorProperty(cycleDuration, saturation, brightness)
}

public fun ComponentProperties<*, *>.siblingBased(
    padding: Float = 0f,
    isInverse: Boolean = false
): SiblingBasedProperty {
    return SiblingBasedProperty(padding, isInverse)
}
