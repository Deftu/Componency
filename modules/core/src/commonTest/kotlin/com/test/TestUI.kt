package com.test

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.impl.Frame
import dev.deftu.componency.components.impl.Rectangle
import dev.deftu.componency.dsl.*
import dev.deftu.componency.platform.Platform

class TestUI(platform: Platform) {

    private val frame = Frame("window") {
        root(platform)
        size(100.percent, 100.percent)

        Rectangle("box") {
            position(25.px, 25.px)
            size(50.percent, 50.percent)
            width = linked(height)
            fill = Color.RED.asProperty

            onPointerClick {
                println("Clicked on $name at $x, $y")
            }
        }
    }

}
