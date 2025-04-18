package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component

public abstract class CancellableComponentEvent(component: Component<*, *>) : ComponentEvent(component) {

    public var isCancelled: Boolean = false
        private set

    public fun cancel() {
        isCancelled = true
    }

}
