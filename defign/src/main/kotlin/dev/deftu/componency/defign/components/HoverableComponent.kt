package dev.deftu.componency.defign.components

import dev.deftu.componency.animations.Easings
import dev.deftu.componency.color.Color
import dev.deftu.componency.components.impl.RectangleComponent
import dev.deftu.componency.defign.DefignPalette
import dev.deftu.componency.dsl.*
import dev.deftu.componency.properties.impl.ContentHuggingProperty
import dev.deftu.componency.styling.Stroke

public open class HoverableComponent @JvmOverloads constructor(
    isDark: Boolean = true
) : RectangleComponent() {

    init {
        configure {
            properties {
                width = ContentHuggingProperty() + 40.px
                height = ContentHuggingProperty() + 20.px
                radius = 5.px
                fill = DefignPalette.get(isDark).background2.asProperty
            }
        }.whenMouseHover {
            component.animate {
                animateStroke(
                    easing = Easings.IN_OUT_QUAD,
                    duration = 200.millis,
                    newValue = Stroke(DefignPalette.get(isDark).primary, 1.px).asProperty
                )
            }
        }.whenMouseUnhover {
            component.animate {
                animateStroke(
                    easing = Easings.IN_OUT_QUAD,
                    duration = 200.millis,
                    newValue = Stroke(Color.TRANSPARENT, 0.px).asProperty
                )
            }
        }
    }

}
