package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component

public abstract class ComponentEvent(public val component: Component) {

    protected var isBubbling: Boolean = true

    public fun stopBubbling() {
        isBubbling = false
    }

}
