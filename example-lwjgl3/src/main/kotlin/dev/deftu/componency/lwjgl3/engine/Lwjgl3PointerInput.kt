package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.input.Cursor
import dev.deftu.componency.input.MouseButton
import dev.deftu.componency.platform.input.PointerInput
import dev.deftu.componency.platform.input.PointerState
import org.lwjgl.glfw.GLFW

class Lwjgl3PointerInput(private val handle: Long) : PointerInput {

    companion object {

        private val mouseButtonMappings = mapOf(
            MouseButton.LEFT to GLFW.GLFW_MOUSE_BUTTON_LEFT,
            MouseButton.RIGHT to GLFW.GLFW_MOUSE_BUTTON_RIGHT,
            MouseButton.MIDDLE to GLFW.GLFW_MOUSE_BUTTON_MIDDLE,
            MouseButton.BACK to GLFW.GLFW_MOUSE_BUTTON_4,
            MouseButton.FORWARD to GLFW.GLFW_MOUSE_BUTTON_5,
            MouseButton.BUTTON6 to GLFW.GLFW_MOUSE_BUTTON_6,
            MouseButton.BUTTON7 to GLFW.GLFW_MOUSE_BUTTON_7,
            MouseButton.BUTTON8 to GLFW.GLFW_MOUSE_BUTTON_8,
        )

        private val cursorMappings = mapOf(
            Cursor.ARROW to GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR),
            Cursor.I_BEAM to GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR),
            // The wait cursor is not available in GLFW
            Cursor.CROSSHAIR to GLFW.glfwCreateStandardCursor(GLFW.GLFW_CROSSHAIR_CURSOR),
            Cursor.HAND to GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR),
            Cursor.RESIZE_HORIZONTAL to GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR),
            Cursor.RESIZE_VERTICAL to GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR),
            Cursor.RESIZE_TOP_LEFT_BOTTOM_RIGHT to GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NWSE_CURSOR),
            Cursor.RESIZE_TOP_RIGHT_BOTTOM_LEFT to GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NESW_CURSOR),
            Cursor.RESIZE_ALL to GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_ALL_CURSOR),
            Cursor.NOT_ALLOWED to GLFW.glfwCreateStandardCursor(GLFW.GLFW_NOT_ALLOWED_CURSOR),
        )

    }

    var mouseX: Float = 0f

    var mouseY: Float = 0f

    override val pointerCount: Int = 1

    fun updatePositions(x: Float, y: Float) {
        mouseX = x
        mouseY = y
    }

    fun getMouseButton(code: Int): MouseButton {
        return mouseButtonMappings.entries.firstOrNull { (_, buttonCode) ->
            buttonCode == code
        }?.key ?: MouseButton.UNKNOWN
    }

    override fun getPointer(index: Int): PointerState {
        if (index != 0) {
            throw IllegalArgumentException("Invalid pointer index: $index")
        }

        val pressedButtons = mutableSetOf<MouseButton>()
        for ((button, code) in mouseButtonMappings) {
            if (GLFW.glfwGetMouseButton(handle, code) == GLFW.GLFW_PRESS) {
                pressedButtons.add(button)
            }
        }

        return PointerState(
            index = index,
            x = mouseX,
            y = mouseY,
            pressedButtons = pressedButtons
        )
    }

}
