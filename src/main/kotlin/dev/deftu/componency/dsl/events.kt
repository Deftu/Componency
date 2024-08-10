package dev.deftu.componency.dsl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.events.*

public fun <T : Component> T.whenMouseClick(listener: MouseClickEvent.() -> Unit): T = apply {
    events.mouseClickListeners.add(listener)
}

public fun <T : Component> T.whenMouseRelease(listener: MouseReleaseEvent.() -> Unit): T = apply {
    events.mouseReleaseListeners.add(listener)
}

public fun <T : Component> T.whenMouseDrag(listener: MouseDragEvent.() -> Unit): T = apply {
    events.mouseDragListeners.add(listener)
}

public fun <T : Component> T.whenMouseScroll(listener: MouseScrollEvent.() -> Unit): T = apply {
    events.mouseScrollListeners.add(listener)
}

public fun <T : Component> T.whenMouseHover(listener: MouseHoverEvent.() -> Unit): T = apply {
    events.mouseHoverListeners.add(listener)
}

public fun <T : Component> T.whenMouseUnhover(listener: MouseHoverEvent.() -> Unit): T = apply {
    events.mouseUnhoverListeners.add(listener)
}

public fun <T : Component> T.whenKeyPress(listener: KeyPressEvent.() -> Unit): T = apply {
    events.keyPressListeners.add(listener)
}
