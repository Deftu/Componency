package dev.deftu.componency.lwjgl3

import dev.deftu.componency.animations.Easings
import dev.deftu.componency.color.Color
import dev.deftu.componency.components.impl.FrameComponent
import dev.deftu.componency.components.impl.RectangleComponent
import dev.deftu.componency.components.impl.input.whenSubmit
import dev.deftu.componency.defign.dsl.useFontBody1
import dev.deftu.componency.dsl.*
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.properties.VectorProperty
import dev.deftu.componency.properties.impl.LinkedProperty

class MyUI(engine: Engine) {

    val frame = FrameComponent().configure {
        name = "window"

        properties {
            width = 100.percent
            height = 100.percent
        }
    }.makeRoot(engine)

    private val box = RectangleComponent().configure {
        name = "box"

        properties {
            x = 25.px
            y = 25.px
            width = 25.percent
            height = LinkedProperty(width as VectorProperty)
            fill = Color.GREEN.asProperty
        }
    }.whenMouseClick {
        println("Box clicked at $x, $y")
    }.attachTo(frame)

    private val input = TestTextInputComponent().configure {
        name = "input"

        properties {
            x = 25.px
            y = siblingBased + 25.px
            width = 25.percent
            height = 25.px
        }.useFontBody1()
    }.whenMouseClick {
        component.requestFocus()
    }.whenSubmit {
        println("Input submitted: $this")
    }.attachTo(frame)

    init {
        box.whenMouseHover {
            println("Mouse entered box at $x, $y")

            component.animate {
                animateWidth(
                    easing = Easings.IN_OUT_CUBIC,
                    duration = 500.millis,
                    newValue = 50.percent
                )
            }
        }.whenMouseUnhover {
            println("Mouse exited box at $x, $y")

            component.animate {
                animateWidth(
                    easing = Easings.IN_OUT_CUBIC,
                    duration = 500.millis,
                    newValue = 25.percent
                )
            }
        }
    }

}
