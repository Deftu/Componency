package dev.deftu.componency.events

abstract class ComponentEvent {
    var isBubbling: Boolean = true

    fun stopBubbling() {
        isBubbling = false
    }
}

abstract class CancellableComponentEvent : ComponentEvent() {
    var isCancelled: Boolean = false

    fun cancel() {
        isCancelled = true
    }
}
