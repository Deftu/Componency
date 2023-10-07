package com.test

import xyz.deftu.componency.ComponencyScreen
import xyz.deftu.componency.animations.Animations
import xyz.deftu.componency.components.impl.*
import xyz.deftu.componency.dsl.*
import xyz.deftu.componency.font.impl.StandardFontProvider
import xyz.deftu.multi.MultiMatrixStack
import xyz.deftu.text.Text
import java.awt.Color
import java.util.*
import kotlin.concurrent.schedule

class TestScreen : ComponencyScreen() {
    private val robotoFontProvider = StandardFontProvider("fonts/Roboto-Regular.ttf")

    val box = BoxComponent().configure {
        // effects += outline(Color.BLUE, 2f)
        filters += gaussianBlur(5f)
        constraints {
            x = 25.px
            y = 25.px
            width = 250.px
            height = 100.px
            color = Color.GREEN.withAlpha(140).toConstraint()
            // radius = 15.px
        }
    }.onMouseLeftClick {
        println("Clicked at $x, $y")
        if (x >= width / 2) { // "width" is the screen width and "x" is the mouse x position
            isCancelled = true // Cancel the event
        }

        client?.setScreen(null)
        true
    }.onMouseScroll {
        println("Scrolled at $x, $y with delta $delta")
        false
    }.attachTo(window)

    val text = TextComponent(Text.create("Hello, World!\nI am on a new line!")).configure {
        constraints {
            x = 5.px
            y = 5.px
            width *= 2f
            // fontProvider = robotoFontProvider
        }
    }.attachTo(box)

    init {
        Timer().schedule(5000) {
            startAnimation()
        }
    }

    private fun startAnimation() {
        text.animate {
            animateWidth(Animations.IN_OUT_CUBIC, 2.5f, width / 2f)
        }

        box.animate {
            val animation = Animations.IN_OUT_CUBIC
            animateX(animation, 2.5f, 25.px(alignOpposite = true))
            animateY(animation, 2.5f, 25.px(alignOpposite = true))
            animateWidth(animation, 2.5f, width / 2f)
            animateHeight(animation, 2.5f, height * 2f)
            animateColor(animation, 2.5f, Color.RED.withAlpha(80).toConstraint())

            whenComplete {
                println("hi")
                text.animate {
                    animateWidth(Animations.IN_OUT_CUBIC, 2.5f, width * 1.25f)
                    whenComplete {
                        println("hi2")
                    }
                }
            }
        }
    }

    override fun handleRender(stack: MultiMatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        // super.handleBackgroundRender(stack)
        super.handleRender(stack, mouseX, mouseY, tickDelta)
    }
}
