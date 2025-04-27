package dev.deftu.componency.lwjgl3.engine

import dev.deftu.componency.image.Image
import dev.deftu.componency.platform.input.ClipboardAccess
import org.lwjgl.glfw.GLFW

class Lwjgl3ClipboardAccess(private val handle: Long) : ClipboardAccess {

    override var clipboardString: String?
        get() = GLFW.glfwGetClipboardString(handle)
        set(value) {
            if (value != null) {
                GLFW.glfwSetClipboardString(handle, value)
            } else {
                GLFW.glfwSetClipboardString(handle, "")
            }
        }

    override var clipboardImage: Image? = null

    override fun clearClipboard() {
        GLFW.glfwSetClipboardString(handle, "")
    }

}
