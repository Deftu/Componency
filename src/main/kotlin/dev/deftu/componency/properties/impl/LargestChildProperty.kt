package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.PaddingProperty
import dev.deftu.componency.properties.SizingProperty

public class LargestChildProperty : SizingProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateWidth(component: Component): Float {
        return component.getChildren().maxOfOrNull { child ->
            child.width + ((child.config.properties.x as? PaddingProperty)?.getHorizontalPadding(child) ?: 0f)
        } ?: 0f
    }

    override fun calculateHeight(component: Component): Float {
        return component.getChildren().maxOfOrNull { child ->
            child.height + ((child.config.properties.y as? PaddingProperty)?.getVerticalPadding(child) ?: 0f)
        } ?: 0f
    }

}
