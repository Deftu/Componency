package dev.deftu.componency.properties

import dev.deftu.componency.components.Component

public interface PaddingProperty {

    public fun getHorizontalPadding(component: Component): Float

    public fun getVerticalPadding(component: Component): Float

}
