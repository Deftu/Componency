package dev.deftu.componency.lwjgl3

import dev.deftu.componency.animations.Easings
import dev.deftu.componency.components.impl.FrameComponent
import dev.deftu.componency.components.impl.RectangleComponent
import dev.deftu.componency.dsl.*
import dev.deftu.componency.engine.Engine
import dev.deftu.componency.properties.VectorProperty
import dev.deftu.componency.properties.impl.LinkedProperty

class MyUI(engine: Engine) {

    private val frame = FrameComponent().configure {
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
        }
    }.whenMouseClick {
        println("Box clicked at $x, $y")
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
        }
    }

    fun render() {
        frame.handleRender()
    }

}
