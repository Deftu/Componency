package dev.deftu.componency.properties

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties

public interface PaddingProperty {

    public fun getHorizontalPadding(component: Component<*, *>): Float

    public fun getVerticalPadding(component: Component<*, *>): Float

}
