package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.PaddingProperty
import dev.deftu.componency.properties.SizingProperty

public class ContentHuggingProperty(
    public val padding: Float = 0f
) : SizingProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateWidth(component: Component<*, *>): Float {
        return component.getChildren().sumOf { child ->
            child.width + ((child.properties.x as? PaddingProperty)?.getHorizontalPadding(child) ?: 0f).toDouble()
        }.toFloat() + (component.getChildren().size - 1) * padding
    }

    override fun calculateHeight(component: Component<*, *>): Float {
        return component.getChildren().sumOf { child ->
            child.height + ((child.properties.y as? PaddingProperty)?.getVerticalPadding(child) ?: 0f).toDouble()
        }.toFloat() + (component.getChildren().size - 1) * padding
    }

}
