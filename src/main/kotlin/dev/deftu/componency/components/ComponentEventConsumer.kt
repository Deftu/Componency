package dev.deftu.componency.components

import dev.deftu.componency.components.events.*
import java.util.function.Consumer

public interface ComponentEventConsumer<T : ComponentEventConsumer<T>> {

    public fun onPointerClick(listener: Consumer<PointerClickEvent>): T
    public fun onPointerRelease(listener: Consumer<PointerReleaseEvent>): T
    public fun onPointerDrag(listener: Consumer<PointerDragEvent>): T
    public fun onScroll(listener: Consumer<ScrollEvent>): T
    public fun onHover(listener: Consumer<HoverEvent>): T
    public fun onUnhover(listener: Consumer<HoverEvent>): T
    public fun onKeyPress(listener: Consumer<KeyPressEvent>): T
    public fun onKeyRelease(listener: Consumer<KeyReleaseEvent>): T
    public fun onCharType(listener: Consumer<CharTypeEvent>): T
    public fun onFocus(listener: Runnable): T
    public fun onUnfocus(listener: Runnable): T

    public fun removePointerClickListener(listener: Consumer<PointerClickEvent>): T
    public fun removePointerReleaseListener(listener: Consumer<PointerReleaseEvent>): T
    public fun removePointerDragListener(listener: Consumer<PointerDragEvent>): T
    public fun removeScrollListener(listener: Consumer<ScrollEvent>): T
    public fun removeHoverListener(listener: Consumer<HoverEvent>): T
    public fun removeUnhoverListener(listener: Consumer<HoverEvent>): T
    public fun removeKeyPressListener(listener: Consumer<KeyPressEvent>): T
    public fun removeKeyReleaseListener(listener: Consumer<KeyReleaseEvent>): T
    public fun removeCharTypeListener(listener: Consumer<CharTypeEvent>): T
    public fun removeFocusListener(listener: Runnable): T
    public fun removeUnfocusListener(listener: Runnable): T

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
