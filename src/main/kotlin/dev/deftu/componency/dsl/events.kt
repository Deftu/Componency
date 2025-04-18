package dev.deftu.componency.dsl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.components.events.*

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onPointerClick(listener: PointerClickEvent.() -> Unit): C = apply {
    this.component.onPointerClick(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onPointerRelease(listener: PointerReleaseEvent.() -> Unit): C = apply {
    this.component.onPointerRelease(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onPointerDrag(listener: PointerDragEvent.() -> Unit): C = apply {
    this.component.onPointerDrag(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onScroll(listener: ScrollEvent.() -> Unit): C = apply {
    this.component.onScroll(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onHover(listener: HoverEvent.() -> Unit): C = apply {
    this.component.onHover(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onUnhover(listener: HoverEvent.() -> Unit): C = apply {
    this.component.onUnhover(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onKeyPress(listener: KeyPressEvent.() -> Unit): C = apply {
    this.component.onKeyPress(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onKeyRelease(listener: KeyReleaseEvent.() -> Unit): C = apply {
    this.component.onKeyRelease(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onCharType(listener: CharTypeEvent.() -> Unit): C = apply {
    this.component.onCharType(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onFocus(listener: () -> Unit): C = apply {
    this.component.onFocus(listener)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.onUnfocus(listener: () -> Unit): C = apply {
    this.component.onUnfocus(listener)
}
