package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.SizingProperty

public class FillProperty(private val useSiblings: Boolean = true) : SizingProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateWidth(component: Component<*, *>): Float {
        if (!component.hasParent) {
            return Component.findPlatform(component).viewportWidth
        }

        val parent = component.parent!!
        return if (useSiblings) {
            parent.width - parent.getChildren().filter { child -> child != component }.sumOf { child ->
                child.width.toDouble() // Kotlin gets all mad if we don't convert to something other than a Float here. The best alternative is to convert to a Double.
            }.toFloat()
        } else {
            parent.right - component.left
        }
    }

    override fun calculateHeight(component: Component<*, *>): Float {
        if (!component.hasParent) {
            return Component.findPlatform(component).viewportHeight
        }

        val parent = component.parent!!
        return if (useSiblings) {
            parent.height - parent.getChildren().filter { child -> child != component }.sumOf { child ->
                child.height.toDouble() // Kotlin gets all mad if we don't convert to something other than a Float here. The best alternative is to convert to a Double.
            }.toFloat()
        } else {
            parent.bottom - component.top
        }
    }

}
