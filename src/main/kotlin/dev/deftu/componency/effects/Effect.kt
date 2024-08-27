package dev.deftu.componency.effects

import dev.deftu.componency.components.Component
import dev.deftu.componency.utils.Animateable

public interface Effect : Animateable {

    public fun preRender(component: Component)

    public fun postRender(component: Component)

}
