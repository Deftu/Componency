package com.test

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.impl.FrameComponent
import dev.deftu.componency.components.impl.RectangleComponent
import dev.deftu.componency.dsl.*
import dev.deftu.componency.engine.Engine

class TestUI(engine: Engine) {

    private val frame = FrameComponent().configure {
        name = "frame"

        properties {
            width = 100.percent
            height = 100.percent
        }
    }.makeRoot(engine)

    private val rect = RectangleComponent().configure {
        name = "rect"

        properties {
            x = 20.px
            y = 20.px
            width = 100.px
            height = 100.px
            fill = Color.RED.asProperty
        }
    }.attachTo(frame)

}
