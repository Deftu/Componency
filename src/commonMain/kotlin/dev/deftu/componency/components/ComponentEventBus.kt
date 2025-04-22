package dev.deftu.componency.components

import dev.deftu.componency.components.events.*

internal class ComponentEventBus<T : Component<T, C>, C : ComponentProperties<T, C>> {

    val pointerClickListeners = mutableListOf<PointerClickEvent.() -> Unit>()
    val pointerReleaseListeners = mutableListOf<PointerReleaseEvent.() -> Unit>()
    val pointerDragListeners = mutableListOf<PointerDragEvent.() -> Unit>()
    val scrollListeners = mutableListOf<ScrollEvent.() -> Unit>()
    val hoverListeners = mutableListOf<HoverEvent.() -> Unit>()
    val unhoverListeners = mutableListOf<HoverEvent.() -> Unit>()
    val keyPressListeners = mutableListOf<KeyPressEvent.() -> Unit>()
    val keyReleaseListeners = mutableListOf<KeyReleaseEvent.() -> Unit>()
    val charTypeListeners = mutableListOf<CharTypeEvent.() -> Unit>()
    val focusListeners = mutableListOf<() -> Unit>()
    val unfocusListeners = mutableListOf<() -> Unit>()

}
