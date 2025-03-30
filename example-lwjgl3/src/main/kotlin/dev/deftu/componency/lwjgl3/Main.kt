package dev.deftu.componency.lwjgl3

import dev.deftu.componency.components.events.KeyboardModifiers
import dev.deftu.componency.defign.DefignFonts
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
        GLFW.glfwSetInputMode(handle, GLFW.GLFW_LOCK_KEY_MODS, GLFW.GLFW_TRUE)

        // Design system
        DefignFonts.preload()

        // Engine
        val engine = Lwjgl3Engine(handle)
        engine.resize(width, height)

        // Create UI
        val ui = MyUI(engine)

        // Inputs
        var mouseX = 0f
        var mouseY = 0f
        GLFW.glfwSetCursorPosCallback(handle) { _, x, y ->
            mouseX = x.toFloat()
            mouseY = y.toFloat()
        }

        GLFW.glfwSetMouseButtonCallback(handle) { handle, button, action, mods ->
            val x = engine.inputEngine.mouseX
            val y = engine.inputEngine.mouseY
            val mouseButton = engine.inputEngine.getMouseButton(button)
            when (action) {
                GLFW.GLFW_PRESS -> ui.frame.handleMouseClick(x.toDouble(), y.toDouble(), mouseButton)
                GLFW.GLFW_RELEASE -> ui.frame.handleMouseRelease()
                else -> Unit // Ignore
            }
        }

        GLFW.glfwSetScrollCallback(handle) { handle, xOff, yOff ->
            ui.frame.handleMouseScroll(xOff)
        }

        GLFW.glfwSetKeyCallback(handle) { handle, keyCode, scancode, action, mods ->
            val key = engine.inputEngine.getKey(keyCode)
            val modifiers = KeyboardModifiers(
                isShift = GLFW.GLFW_MOD_SHIFT and mods != 0,
                isCtrl = GLFW.GLFW_MOD_CONTROL and mods != 0,
                isAlt = GLFW.GLFW_MOD_ALT and mods != 0,
                isSuper = GLFW.GLFW_MOD_SUPER and mods != 0,
                isCapsLock = GLFW.GLFW_MOD_CAPS_LOCK and mods != 0,
                isNumLock = GLFW.GLFW_MOD_NUM_LOCK and mods != 0,
            )

            when (action) {
                GLFW.GLFW_PRESS -> ui.frame.handleKeyPress(key, modifiers)
                GLFW.GLFW_RELEASE -> ui.frame.handleKeyRelease(key, modifiers)
                else -> Unit // Ignore
            }
        }

        GLFW.glfwSetCharModsCallback(handle) { handle, codepoint, mods ->
            val modifiers = KeyboardModifiers(
                isShift = GLFW.GLFW_MOD_SHIFT and mods != 0,
                isCtrl = GLFW.GLFW_MOD_CONTROL and mods != 0,
                isAlt = GLFW.GLFW_MOD_ALT and mods != 0,
                isSuper = GLFW.GLFW_MOD_SUPER and mods != 0,
                isCapsLock = GLFW.GLFW_MOD_CAPS_LOCK and mods != 0,
                isNumLock = GLFW.GLFW_MOD_NUM_LOCK and mods != 0,
            )

            if (Character.charCount(codepoint) == 1) {
                ui.frame.handleCharType(codepoint.toChar(), modifiers)
            } else {
                for (char in Character.toChars(codepoint)) {
                    ui.frame.handleCharType(char, modifiers)
                }
            }
        }

        // Render loop
        GLFW.glfwMakeContextCurrent(handle)
        GL.createCapabilities()

        GL11.glClearColor(0f, 0f, 0f, 0f)
        while (!GLFW.glfwWindowShouldClose(handle)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

            engine.updateMousePosition(mouseX, mouseY)
            ui.frame.handleRender()

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
