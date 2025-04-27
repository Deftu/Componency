@file:Suppress("UNCHECKED_CAST")

package dev.deftu.componency.components

import dev.deftu.componency.components.events.*

public interface ComponentEventConsumer<T : ComponentEventConsumer<T>> : ComponentEventConsumerPlatformAware<T> {

    override val self: T
        get() = this as T

    public fun onPointerClick(listener: PointerClickEvent.() -> Unit): T
    public fun onPointerRelease(listener: PointerReleaseEvent.() -> Unit): T
    public fun onPointerDrag(listener: PointerDragEvent.() -> Unit): T
    public fun onScroll(listener: ScrollEvent.() -> Unit): T
    public fun onHover(listener: HoverEvent.() -> Unit): T
    public fun onUnhover(listener: HoverEvent.() -> Unit): T
    public fun onKeyPress(listener: KeyPressEvent.() -> Unit): T
    public fun onKeyRelease(listener: KeyReleaseEvent.() -> Unit): T
    public fun onCharType(listener: CharTypeEvent.() -> Unit): T
    public fun onFocus(listener: () -> Unit): T
    public fun onUnfocus(listener: () -> Unit): T

    public fun removePointerClickListener(listener: PointerClickEvent.() -> Unit): T
    public fun removePointerReleaseListener(listener: PointerReleaseEvent.() -> Unit): T
    public fun removePointerDragListener(listener: PointerDragEvent.() -> Unit): T
    public fun removeScrollListener(listener: ScrollEvent.() -> Unit): T
    public fun removeHoverListener(listener: HoverEvent.() -> Unit): T
    public fun removeUnhoverListener(listener: HoverEvent.() -> Unit): T
    public fun removeKeyPressListener(listener: KeyPressEvent.() -> Unit): T
    public fun removeKeyReleaseListener(listener: KeyReleaseEvent.() -> Unit): T
    public fun removeCharTypeListener(listener: CharTypeEvent.() -> Unit): T
    public fun removeFocusListener(listener: () -> Unit): T
    public fun removeUnfocusListener(listener: () -> Unit): T

    public fun firePointerClickEvent(event: PointerClickEvent)
    public fun firePointerReleaseEvent(event: PointerReleaseEvent)
    public fun firePointerDragEvent(event: PointerDragEvent)
    public fun fireScrollEvent(event: ScrollEvent)
    public fun fireHoverEvent(event: HoverEvent)
    public fun fireUnhoverEvent(event: HoverEvent)
    public fun fireKeyPressEvent(event: KeyPressEvent)
    public fun fireKeyReleaseEvent(event: KeyReleaseEvent)
    public fun fireCharTypeEvent(event: CharTypeEvent)
    public fun fireFocusEvent()
    public fun fireUnfocusEvent()

}
