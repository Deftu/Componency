package com.test

import dev.deftu.componency.ComponencyScreen
import dev.deftu.componency.animations.Animations
import dev.deftu.componency.components.impl.*
import dev.deftu.componency.dsl.*
import dev.deftu.componency.font.impl.StandardFontProvider
import dev.deftu.multi.MultiKeyboard
import dev.deftu.text.Text
import dev.deftu.text.TextFactory
import java.awt.Color

class TestScreen : ComponencyScreen() {
    private val textFactory = TextFactory()
    private val robotoFontProvider = StandardFontProvider("fonts/Roboto-Regular.ttf")

    val box = BoxComponent().configure {
        constraints {
            x = 25.px
            y = 25.px
            width = 250.px
            height = 100.px
            color = Color.GREEN.withAlpha(140).toConstraint()
            // radius = 15.px
        }
    }.addMouseLeftClickListener { event ->
        println("Clicked at ${event.x}, ${event.y}")
        if (event.x >= width / 2) { // "width" is the screen width and "x" is the mouse x position
            event.cancel() // Cancel the event
        }

        client?.setScreen(null)
    }.addMouseScrollListener { event ->
        println("Scrolled at ${event.x}, ${event.y} with delta ${event.delta}")
    }.attachTo(window)

    val text = TextComponent(textFactory.create("Hello, World!\nI am on a new line!")).configure {
        constraints {
            x = 5.px
            y = 5.px
            width *= 2f
            fontProvider = robotoFontProvider
        }
    }.attachTo(box)

    init {
        window.addKeyPressListener { event ->
            if (event.keyCode == MultiKeyboard.KEY_J) {
                startAnimation()
            }
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
                println("Finished animation!")
                text.animate {
                    animateWidth(Animations.IN_OUT_CUBIC, 2.5f, width * 1.25f)
                    whenComplete {
                        println("Finished text animation!")
                    }
                }
            }
        }
    }
}
