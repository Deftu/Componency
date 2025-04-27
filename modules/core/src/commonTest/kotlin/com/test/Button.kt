package com.test

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.components.impl.*
import dev.deftu.componency.dsl.*

fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Button(
    name: String? = null,
    block: RectangleComponentProperties.() -> Unit = {}
): C = apply {
    return Rectangle(name) {
        size(hugging + 20.px, hugging + 10.px)
        fill = Color.RED.asProperty

        Text {
            position(centered, centered)
            fill = Color.WHITE.asProperty
            // TODO: font
            fontSize = 12.px
        }

        block()
    }
}
