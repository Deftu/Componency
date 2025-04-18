package dev.deftu.componency.lwjgl3

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.events.KeyboardModifiers
import dev.deftu.componency.components.impl.Frame
import dev.deftu.componency.components.impl.Rectangle
import dev.deftu.componency.components.traits.focusable
import dev.deftu.componency.dsl.*
import dev.deftu.componency.easings.Easings
import dev.deftu.componency.lwjgl3.engine.Lwjgl3Platform
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

        // Engine
        val platform = Lwjgl3Platform(handle)
        platform.viewportWidth = width.toFloat()
        platform.viewportHeight = height.toFloat()

        // Create UI
        val frame = Frame("window") {
            root(platform)
            size(100.percent, 100.percent)

            Rectangle("button") {
                size(25.percent, 25.percent)
                position(25.percent, 25.percent)
                fill = Color.GREEN.asProperty

                onPointerClick {
                    println("Button clicked @ $x, $y")
                }

                onFocus {
                    println("Button focused!")

                    val targetWidth = 50.percent
                    this.width.animateTo(Easings.LINEAR, 1.5.seconds, targetWidth)
                }

                onUnfocus {
                    println("Button unfocused!")
                    val targetWidth = 25.percent
                    this.width.animateTo(Easings.IN_OUT_QUAD, 5.seconds, targetWidth)
                }
            }.focusable()
        }

        // Inputs
        var mouseX = 0f
        var mouseY = 0f
        GLFW.glfwSetCursorPosCallback(handle) { _, x, y ->
            mouseX = x.toFloat()
            mouseY = y.toFloat()
        }

        GLFW.glfwSetMouseButtonCallback(handle) { handle, button, action, mods ->
            val x = platform.inputHandler.pointerInput.mouseX
            val y = platform.inputHandler.pointerInput.mouseY
            val mouseButton = platform.inputHandler.pointerInput.getMouseButton(button)
            when (action) {
                GLFW.GLFW_PRESS -> frame.handlePointerClick(x.toDouble(), y.toDouble(), mouseButton)
                GLFW.GLFW_RELEASE -> frame.handlePointerRelease()
                else -> Unit // Ignore
            }
        }

        GLFW.glfwSetScrollCallback(handle) { handle, xOff, yOff ->
            frame.handleMouseScroll(xOff)
        }

        GLFW.glfwSetKeyCallback(handle) { handle, keyCode, scancode, action, mods ->
            val key = platform.inputHandler.keyboardInput.getKey(keyCode)
            val modifiers = KeyboardModifiers(
                isShift = GLFW.GLFW_MOD_SHIFT and mods != 0,
                isCtrl = GLFW.GLFW_MOD_CONTROL and mods != 0,
                isAlt = GLFW.GLFW_MOD_ALT and mods != 0,
                isSuper = GLFW.GLFW_MOD_SUPER and mods != 0,
                isCapsLock = GLFW.GLFW_MOD_CAPS_LOCK and mods != 0,
                isNumLock = GLFW.GLFW_MOD_NUM_LOCK and mods != 0,
            )

            when (action) {
                GLFW.GLFW_PRESS -> frame.handleKeyPress(key, modifiers)
                GLFW.GLFW_RELEASE -> frame.handleKeyRelease(key, modifiers)
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
                frame.handleCharType(codepoint.toChar(), modifiers)
            } else {
                for (char in Character.toChars(codepoint)) {
                    frame.handleCharType(char, modifiers)
                }
            }
        }

        // Render loop
        GLFW.glfwMakeContextCurrent(handle)
        GL.createCapabilities()

        GL11.glClearColor(0f, 0f, 0f, 0f)
        while (!GLFW.glfwWindowShouldClose(handle)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

            platform.inputHandler.pointerInput.updatePositions(mouseX, mouseY)
            frame.handleRender()

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
