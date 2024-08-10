package dev.deftu.componency.properties.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.properties.ColorProperty
import java.awt.Color

public class RainbowColorProperty : ColorProperty {

    override var cachedValue: Color = Color.WHITE
    override var needsRecalculate: Boolean = true

    override fun calculateColor(component: Component): Color {
        return Color.getHSBColor((System.currentTimeMillis() % 1000).toFloat() / 1000, 1f, 1f)
    }

}
