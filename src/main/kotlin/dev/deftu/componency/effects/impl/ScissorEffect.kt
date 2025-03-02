package dev.deftu.componency.effects.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.effects.Effect
import dev.deftu.componency.engine.Engine

public class ScissorEffect : Effect {

    override fun preRender(component: Component) {
        val engine = Engine.get(component)

        engine.renderEngine.pushScissor(
            x1 = component.left,
            y1 = component.top,
            x2 = component.right,
            y2 = component.bottom
        )
    }

    override fun postRender(component: Component) {
        val engine = Engine.get(component)

        engine.renderEngine.popScissor()
    }

    override fun frame() {
        // no-op
    }

    override fun recalculate() {
        // no-op
    }

}
