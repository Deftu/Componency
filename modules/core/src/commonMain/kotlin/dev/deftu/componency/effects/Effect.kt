package dev.deftu.componency.effects

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties

public interface Effect<T : Component<T, C>, C : ComponentProperties<T, C>> {

    public fun preRender(component: Component<T, C>)

    public fun postRender(component: Component<T, C>)

    public fun animationFrame(deltaTime: Float) {
    }

    public fun recalculate() {
    }

}
