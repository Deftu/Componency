@file:Suppress("UNCHECKED_CAST")

package dev.deftu.componency.components

import dev.deftu.componency.components.events.*
import java.util.function.Consumer

public actual interface ComponentEventConsumerPlatformAware<T : ComponentEventConsumer<T>> {

    public actual val self: T

    public fun onPointerClick(listener: Consumer<PointerClickEvent>): T = apply {
        self.onPointerClick(listener::accept)
    } as T

    public fun onPointerRelease(listener: Consumer<PointerReleaseEvent>): T = apply {
        self.onPointerRelease(listener::accept)
    } as T

    public fun onPointerDrag(listener: Consumer<PointerDragEvent>): T = apply {
        self.onPointerDrag(listener::accept)
    } as T

    public fun onScroll(listener: Consumer<ScrollEvent>): T = apply {
        self.onScroll(listener::accept)
    } as T

    public fun onHover(listener: Consumer<HoverEvent>): T = apply {
        self.onHover(listener::accept)
    } as T

    public fun onUnhover(listener: Consumer<HoverEvent>): T = apply {
        self.onUnhover(listener::accept)
    } as T

    public fun onKeyPress(listener: Consumer<KeyPressEvent>): T = apply {
        self.onKeyPress(listener::accept)
    } as T

    public fun onKeyRelease(listener: Consumer<KeyReleaseEvent>): T = apply {
        self.onKeyRelease(listener::accept)
    } as T

    public fun onCharType(listener: Consumer<CharTypeEvent>): T = apply {
        self.onCharType(listener::accept)
    } as T

    public fun onFocus(listener: Runnable): T = apply {
        self.onFocus(listener::run)
    } as T

    public fun onUnfocus(listener: Runnable): T = apply {
        self.onUnfocus(listener::run)
    } as T

    public fun removePointerClickListener(listener: Consumer<PointerClickEvent>): T = apply {
        self.removePointerClickListener(listener::accept)
    } as T

    public fun removePointerReleaseListener(listener: Consumer<PointerReleaseEvent>): T = apply {
        self.removePointerReleaseListener(listener::accept)
    } as T

    public fun removePointerDragListener(listener: Consumer<PointerDragEvent>): T = apply {
        self.removePointerDragListener(listener::accept)
    } as T

    public fun removeScrollListener(listener: Consumer<ScrollEvent>): T = apply {
        self.removeScrollListener(listener::accept)
    } as T

    public fun removeHoverListener(listener: Consumer<HoverEvent>): T = apply {
        self.removeHoverListener(listener::accept)
    } as T

    public fun removeUnhoverListener(listener: Consumer<HoverEvent>): T = apply {
        self.removeUnhoverListener(listener::accept)
    } as T

    public fun removeKeyPressListener(listener: Consumer<KeyPressEvent>): T = apply {
        self.removeKeyPressListener(listener::accept)
    } as T

    public fun removeKeyReleaseListener(listener: Consumer<KeyReleaseEvent>): T = apply {
        self.removeKeyReleaseListener(listener::accept)
    } as T

    public fun removeCharTypeListener(listener: Consumer<CharTypeEvent>): T = apply {
        self.removeCharTypeListener(listener::accept)
    } as T

    public fun removeFocusListener(listener: Runnable): T = apply {
        self.removeFocusListener(listener::run)
    } as T

    public fun removeUnfocusListener(listener: Runnable): T = apply {
        self.removeUnfocusListener(listener::run)
    } as T

}
