package dev.deftu.componency.lwjgl3

import dev.deftu.componency.lwjgl3.engine.Lwjgl3Engine
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        if (!GLFW.glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        val width = 800
        val height = 600
        val handle = createWindow("Hello World!", width, height)
        centerWindow(handle)

        GLFW.glfwSwapInterval(0)
        GLFW.glfwShowWindow(handle)

        // Inputs
        var mouseX = 0f
        var mouseY = 0f
        GLFW.glfwSetCursorPosCallback(handle) { _, x, y ->
            mouseX = x.toFloat()
            mouseY = y.toFloat()
        }

        // Engine
        val engine = Lwjgl3Engine()
        engine.resize(width, height)

        // Create UI
        val ui = MyUI(engine)

        // Render loop
        GLFW.glfwMakeContextCurrent(handle)
        GL.createCapabilities()

        GL11.glClearColor(0f, 0f, 0f, 0f)
        while (!GLFW.glfwWindowShouldClose(handle)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

            engine.updateMousePosition(mouseX, mouseY)
            ui.render()

            GLFW.glfwSwapBuffers(handle)
            GLFW.glfwPollEvents()
        }
    }

    private fun createWindow(title: String, width: Int, height: Int): Long {
        GLFW.glfwDefaultWindowHints()
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3)
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE)
        val handle = GLFW.glfwCreateWindow(width, height, title, 0, 0)
        if (handle == MemoryUtil.NULL) {
            throw IllegalStateException("Failed to create window!")
        }

        return handle
    }

    private fun centerWindow(handle: Long) {
        MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)
            GLFW.glfwGetWindowSize(handle, pWidth, pHeight)

            val vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor())
            val x = (vidMode!!.width() - pWidth.get(0)) / 2
            val y = (vidMode.height() - pHeight.get(0)) / 2
            GLFW.glfwSetWindowPos(handle, x, y)
        }
    }

}
