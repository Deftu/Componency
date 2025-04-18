package dev.deftu.componency.components

import dev.deftu.componency.components.events.*
import java.util.function.Consumer

internal class ComponentEventBus<T : Component<T, C>, C : ComponentProperties<T, C>> {

    val pointerClickListeners = mutableListOf<Consumer<PointerClickEvent>>()
    val pointerReleaseListeners = mutableListOf<Consumer<PointerReleaseEvent>>()
    val pointerDragListeners = mutableListOf<Consumer<PointerDragEvent>>()
    val scrollListeners = mutableListOf<Consumer<ScrollEvent>>()
    val hoverListeners = mutableListOf<Consumer<HoverEvent>>()
    val unhoverListeners = mutableListOf<Consumer<HoverEvent>>()
    val keyPressListeners = mutableListOf<Consumer<KeyPressEvent>>()
    val keyReleaseListeners = mutableListOf<Consumer<KeyReleaseEvent>>()
    val charTypeListeners = mutableListOf<Consumer<CharTypeEvent>>()
    val focusListeners = mutableListOf<Runnable>()
    val unfocusListeners = mutableListOf<Runnable>()

}
