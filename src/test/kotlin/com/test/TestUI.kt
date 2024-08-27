package com.test

import dev.deftu.componency.components.impl.FrameComponent
import dev.deftu.componency.components.impl.RectangleComponent
import dev.deftu.componency.dsl.*
import dev.deftu.componency.engine.Engine
import java.awt.Color

class TestUI(engine: Engine) {

    private val frame = FrameComponent().configure {
        name = "frame"
        hidden = false
        clipping = false

        properties {
            width = 100.percent
            height = 100.percent
        }
    }.makeRoot(engine)

    private val rect = RectangleComponent().configure {
        name = "rect"
        hidden = false
        clipping = false

        properties {
            x = 20.px
            y = 20.px
            width = 100.px
            height = 100.px
            fill = Color.RED.asProperty
        }
    }.attachTo(frame)

}
