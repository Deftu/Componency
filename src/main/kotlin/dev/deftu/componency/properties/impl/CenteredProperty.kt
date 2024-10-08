package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.properties.PositionalProperty

public class CenteredProperty : PositionalProperty {

    override var cachedValue: Float = 0f
    override var needsRecalculate: Boolean = true

    override fun calculateX(component: Component): Float {
        val engine = Engine.get(component)
        val value = if (component.hasParent) {
            val parent = component.parent!!
            if (component.isAlreadyCentered) {
                (parent.left + (parent.width / 2))
            } else {
                (parent.left + (parent.width / 2 - component.width / 2))
            }
        } else {
            component.width / 2 - component.width / 2
        }

        return engine.renderEngine.roundToPixel(value)
    }

    override fun calculateY(component: Component): Float {
        val engine = Engine.get(component)
        val value = if (component.hasParent) {
            val parent = component.parent!!
            if (component.isAlreadyCentered) {
                (parent.top + (parent.height / 2))
            } else {
                (parent.top + (parent.height / 2 - component.height / 2))
            }
        } else {
            component.height / 2 - component.height / 2
        }

        return engine.renderEngine.roundToPixel(value)
    }

}
