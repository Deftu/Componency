@file:Suppress("SameParameterValue")

package dev.deftu.componency.lwjgl3

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.events.KeyboardModifiers
import dev.deftu.componency.components.impl.Frame
import dev.deftu.componency.components.impl.Gif
import dev.deftu.componency.components.impl.Rectangle
import dev.deftu.componency.components.traits.focusable
import dev.deftu.componency.dsl.*
import dev.deftu.componency.easings.Easings
import dev.deftu.componency.gif.GifAnimation
import dev.deftu.componency.lwjgl3.engine.Lwjgl3Platform
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil
import kotlin.io.path.Path

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        if (!GLFW.glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        var windowWidth = 800
        var windowHeight = 600
        val handle = createWindow("Hello World!", windowWidth, windowHeight)
        centerWindow(handle)

        GLFW.glfwShowWindow(handle)
        GLFW.glfwSetInputMode(handle, GLFW.GLFW_LOCK_KEY_MODS, GLFW.GLFW_TRUE)

        // Engine
        val platform = Lwjgl3Platform(handle)
        platform.audioEngine.initialize()

        // Set up platform framebuffer size
        val framebufferWidth = IntArray(1)
        val framebufferHeight = IntArray(1)
        GLFW.glfwGetFramebufferSize(handle, framebufferWidth, framebufferHeight)
        platform.viewportWidth = framebufferWidth[0].toFloat()
        platform.viewportHeight = framebufferHeight[0].toFloat()
        platform.pixelRatio = platform.viewportWidth / windowWidth.toFloat()

        // Create UI
        val frame = Frame("window") {
            root(platform)
            debugger()
            size(100.percent, 100.percent)

            Rectangle("button") {
                size(50.percent, 50.percent)
                position(centered, 25.percent)
                fill = Color.GREEN.asProperty

                onPointerClick {
                    println("Button clicked @ $x, $y")

                    component.requestFocus()
                    cancel()
                }

                onFocus {
                    println("Button focused!")

                    val targetWidth = 75.percent
                    this.width.animateTo(Easings.LINEAR, 1.5.seconds, targetWidth)
                }

                onUnfocus {
                    println("Button unfocused!")
                    val targetWidth = 50.percent
                    this.width.animateTo(Easings.IN_OUT_QUAD, 3.seconds, targetWidth)
                }

                Gif("animation1") {
                    size(25.percent, 25.percent)
                    position(centered, 10.px)
                    fill = Color.WHITE.asProperty
                    animation = GifAnimation.from(Path("D:\\Downloads\\test.gif"))

                    onPointerClick {
                        println("Gif clicked @ $x, $y")

                        component.requestFocus()
                        cancel()
                    }

                    onFocus {
                        println("Gif focused!")

                        val targetWidth = 50.percent
                        this.width.animateTo(Easings.LINEAR, 1.5.seconds, targetWidth)
                    }

                    onUnfocus {
                        println("Gif unfocused!")
                        val targetWidth = 25.percent
                        this.width.animateTo(Easings.IN_OUT_QUAD, 3.seconds, targetWidth)
                    }
                }.focusable()

                Gif("animation2") {
                    size(25.percent, 25.percent)
                    position(centered, 10.px(isInverse = true))
                    fill = Color.WHITE.asProperty
                    animation = GifAnimation.from(Path("D:\\Downloads\\test.gif"))

                    onPointerClick {
                        println("Gif clicked @ $x, $y")

                        component.requestFocus()
                        cancel()
                    }

                    onFocus {
                        println("Gif focused!")

                        val targetWidth = 50.percent
                        this.width.animateTo(Easings.LINEAR, 1.5.seconds, targetWidth)
                    }

                    onUnfocus {
                        println("Gif unfocused!")
                        val targetWidth = 25.percent
                        this.width.animateTo(Easings.IN_OUT_QUAD, 3.seconds, targetWidth)
                    }
                }.focusable()
            }.focusable()
        }

        // Window state
        GLFW.glfwSetWindowSizeCallback(handle) { _, width, height ->
            windowWidth = width
            windowHeight = height
        }

        GLFW.glfwSetFramebufferSizeCallback(handle) { _, width, height ->
            println("Framebuffer size changed: $width x $height")
            platform.viewportWidth = width.toFloat()
            platform.viewportHeight = height.toFloat()

            val newWindowWidth = IntArray(1)
            GLFW.glfwGetWindowSize(handle, newWindowWidth, null)
            platform.pixelRatio = width.toFloat() / newWindowWidth[0].toFloat()

            frame.recalculate() // Force a recalculation of the layout
        }

        // Inputs
        var mouseX = 0f
        var mouseY = 0f
        GLFW.glfwSetCursorPosCallback(handle) { _, x, y ->
            mouseX = x.toFloat()
            mouseY = y.toFloat()
        }

        GLFW.glfwSetMouseButtonCallback(handle) { _, button, action, _ ->
            val x = platform.inputHandler.pointerInput.mouseX
            val y = platform.inputHandler.pointerInput.mouseY
            val mouseButton = platform.inputHandler.pointerInput.getMouseButton(button)
            when (action) {
                GLFW.GLFW_PRESS -> frame.handlePointerClick(x.toDouble(), y.toDouble(), mouseButton)
                GLFW.GLFW_RELEASE -> frame.handlePointerRelease()
                else -> Unit // Ignore
            }
        }

        GLFW.glfwSetScrollCallback(handle) { _, xOff, _ ->
            frame.handleMouseScroll(xOff)
        }

        GLFW.glfwSetKeyCallback(handle) { _, keyCode, _, action, mods ->
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

        GLFW.glfwSetCharModsCallback(handle) { _, codepoint, mods ->
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
        GLFW.glfwSwapInterval(0)
        GL.createCapabilities()

        GL11.glClearColor(0f, 0f, 0f, 0f)
        while (!GLFW.glfwWindowShouldClose(handle)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)
            GL11.glViewport(0, 0, windowWidth, windowHeight)

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
