package dev.deftu.componency.effects

import dev.deftu.componency.components.Component
import dev.deftu.componency.utils.Animateable
import dev.deftu.componency.utils.Recalculable

public interface Effect : Animateable, Recalculable {

    public fun preRender(component: Component)

    public fun postRender(component: Component)

}
