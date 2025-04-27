package dev.deftu.componency.effects.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.effects.Effect

public class ScissorEffect<T : Component<T, C>, C : ComponentProperties<T, C>> : Effect<T, C> {

    override fun preRender(component: Component<T, C>) {
        val platform = Component.findPlatform(component)

        platform.renderer.pushScissor(
            x1 = component.left,
            y1 = component.top,
            x2 = component.right,
            y2 = component.bottom
        )
    }

    override fun postRender(component: Component<T, C>) {
        val platform = Component.findPlatform(component)
        platform.renderer.popScissor()
    }

}
