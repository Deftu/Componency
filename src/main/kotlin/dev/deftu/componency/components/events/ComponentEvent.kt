package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component

public abstract class ComponentEvent(public open val component: Component<*, *>) {

    public var isBubbling: Boolean = true
        protected set

    public fun stopBubbling() {
        isBubbling = false
    }

}
