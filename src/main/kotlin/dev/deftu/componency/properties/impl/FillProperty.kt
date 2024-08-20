package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.properties.SizingProperty

public class FillProperty(private val useSiblings: Boolean = true) : SizingProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateWidth(component: Component): Float {
        return if (component.hasParent) {
            val parent = component.parent!!
            if (useSiblings) {
                parent.width - parent.getChildren().filter { child -> child != component }.sumOf { child ->
                    child.width.toDouble() // Kotlin gets all mad if we don't convert to something other than a Float here. The best alternative is to convert to a Double.
                }.toFloat()
            } else {
                parent.right - component.left
            }
        } else {
            Engine.get(component).renderEngine.viewportWidth.toFloat()
        }
    }

    override fun calculateHeight(component: Component): Float {
        return if (component.hasParent) {
            val parent = component.parent!!
            if (useSiblings) {
                parent.height - parent.getChildren().filter { child -> child != component }.sumOf { child ->
                    child.height.toDouble() // Kotlin gets all mad if we don't convert to something other than a Float here. The best alternative is to convert to a Double.
                }.toFloat()
            } else {
                parent.bottom - component.top
            }
        } else {
            Engine.get(component).renderEngine.viewportHeight.toFloat()
        }
    }

}
