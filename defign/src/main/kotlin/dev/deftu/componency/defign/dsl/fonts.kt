package dev.deftu.componency.defign.dsl

import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.defign.DefignFonts

public fun ComponentProperties.useFontAttention1(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useAttention1(this, scalingFactor)
}

public fun ComponentProperties.useFontAttention2(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useAttention2(this, scalingFactor)
}

public fun ComponentProperties.useFontAttention3(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useAttention3(this, scalingFactor)
}

public fun ComponentProperties.useFontHeader1(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useHeader1(this, scalingFactor)
}

public fun ComponentProperties.useFontHeader2(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useHeader2(this, scalingFactor)
}

public fun ComponentProperties.useFontHeader3(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useHeader3(this, scalingFactor)
}

public fun ComponentProperties.useFontTitle1(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useTitle1(this, scalingFactor)
}

public fun ComponentProperties.useFontTitle2(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useTitle2(this, scalingFactor)
}

public fun ComponentProperties.useFontTitle3(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useTitle3(this, scalingFactor)
}

public fun ComponentProperties.useFontBody1(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useBody1(this, scalingFactor)
}

public fun ComponentProperties.useFontBody2(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useBody2(this, scalingFactor)
}

public fun ComponentProperties.useFontBody3(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useBody3(this, scalingFactor)
}

public fun ComponentProperties.useFontCaption1(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useCaption1(this, scalingFactor)
}

public fun ComponentProperties.useFontCaption2(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useCaption2(this, scalingFactor)
}

public fun ComponentProperties.useFontCaption3(scalingFactor: Float = 1f): ComponentProperties = apply {
    DefignFonts.useCaption3(this, scalingFactor)
}
