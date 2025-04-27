package dev.deftu.componency.components

import dev.deftu.componency.input.MouseButton

public class PointerTracker<T : Component<T, C>, C : ComponentProperties<T, C>>(private val component: Component<T, C>) {

    public var isCurrentlyHovered: Boolean = false
        internal set

    public var lastHoveredX: Double = 0.0
        private set
    public var lastHoveredY: Double = 0.0
        private set

    public var lastDraggedX: Double = 0.0
        private set
    public var lastDraggedY: Double = 0.0
        private set

    public var currentClickCount: Int = 0
        internal set

    public var lastClickedButton: MouseButton = MouseButton.UNKNOWN
        internal set

    public var lastClickedTime: Long = 0L
        internal set

    internal fun handlePointerMove(x: Double, y: Double, isHovered: Boolean): HoverStateChange? {
        updateHoverPosition(x, y)

        return when {
            isHovered && !isCurrentlyHovered -> {
                isCurrentlyHovered = true
                HoverStateChange.ENTERED
            }

            !isHovered && isCurrentlyHovered -> {
                isCurrentlyHovered = false
                HoverStateChange.EXITED
            }

            else -> null
        }
    }

    internal fun handlePointerClick(x: Double, y: Double, button: MouseButton) {
        updateDragPosition(x, y)
        currentClickCount = if (component.clock.nowMillis() - lastClickedTime < 500) currentClickCount + 1 else 1
        lastClickedButton = button
        lastClickedTime = component.clock.nowMillis()
    }

    internal fun handlePointerRelease() {
        updateDragPosition(-1.0, -1.0)
        lastClickedButton = MouseButton.UNKNOWN
    }

    internal fun updateDragPosition(x: Double, y: Double) {
        lastDraggedX = x
        lastDraggedY = y
    }

    private fun updateHoverPosition(x: Double, y: Double) {
        lastHoveredX = x
        lastHoveredY = y
    }

    internal enum class HoverStateChange {
        ENTERED,
        EXITED
    }

}
