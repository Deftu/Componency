package dev.deftu.componency.defign.dsl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.defign.DefignFonts

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontAttention1(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyAttention1(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontAttention2(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyAttention2(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontAttention3(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyAttention3(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontHeader1(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyHeader1(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontHeader2(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyHeader2(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontHeader3(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyHeader3(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontTitle1(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyTitle1(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontTitle2(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyTitle2(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontTitle3(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyTitle3(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontBody1(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyBody1(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontBody2(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyBody2(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontBody3(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyBody3(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontCaption1(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyCaption1(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontCaption2(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyCaption2(this, scalingFactor)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.applyFontCaption3(scalingFactor: Float = 1f): C = apply {
    DefignFonts.applyCaption3(this, scalingFactor)
}
