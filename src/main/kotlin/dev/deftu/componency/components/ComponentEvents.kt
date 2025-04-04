package dev.deftu.componency.components

import dev.deftu.componency.components.events.*

public open class ComponentEvents {

    public val mouseClickListeners: MutableList<(MouseClickEvent) -> Unit> = mutableListOf()
    public val mouseReleaseListeners: MutableList<(MouseReleaseEvent) -> Unit> = mutableListOf()
    public val mouseDragListeners: MutableList<(MouseDragEvent) -> Unit> = mutableListOf()
    public val mouseScrollListeners: MutableList<(MouseScrollEvent) -> Unit> = mutableListOf()
    public val mouseHoverListeners: MutableList<(MouseHoverEvent) -> Unit> = mutableListOf()
    public val mouseUnhoverListeners: MutableList<(MouseHoverEvent) -> Unit> = mutableListOf()
    public val keyPressListeners: MutableList<(KeyPressEvent) -> Unit> = mutableListOf()
    public val keyReleaseListeners: MutableList<(KeyReleaseEvent) -> Unit> = mutableListOf()
    public val charTypeListeners : MutableList<(CharTypeEvent) -> Unit> = mutableListOf()
    public val focusListeners: MutableList<() -> Unit> = mutableListOf()
    public val unfocusListeners: MutableList<() -> Unit> = mutableListOf()

}
