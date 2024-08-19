@file:Suppress("UNCHECKED_CAST", "LeakingThis")

package dev.deftu.componency.defign.components

import dev.deftu.componency.animations.Easings
import dev.deftu.componency.components.Component
import dev.deftu.componency.components.impl.CircleComponent
import dev.deftu.componency.components.impl.FrameComponent
import dev.deftu.componency.components.impl.RectangleComponent
import dev.deftu.componency.defign.DefignPalette
import dev.deftu.componency.dsl.*
import dev.deftu.componency.properties.ColorProperty
import dev.deftu.componency.properties.XProperty
import dev.deftu.componency.properties.impl.CenteredProperty
import dev.deftu.componency.styling.Stroke
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf
import java.util.function.Consumer

public open class ToggleComponent @JvmOverloads constructor(
    valueState: State<Boolean>,
    private val isDark: Boolean = true
) : FrameComponent() {

    public companion object {

        public const val DEFAULT_WIDTH: Float = 32.5f
        public const val DEFAULT_HEIGHT: Float = 15f

    }

    public val changeListeners: MutableList<(Boolean) -> Unit> = mutableListOf()

    private val valueState = mappedMutableStateOf(valueState) { it }

    public var value: Boolean by mutableStateBound(this.valueState)

    private val trackColor: ColorProperty
        get() {
            return if (value) {
                DefignPalette.get(isDark).primary.asProperty
            } else {
                DefignPalette.get(isDark).background2.asProperty
            }
        }

    private val thumbColor: ColorProperty
        get() {
            return if (value) {
                DefignPalette.get(isDark).background2.asProperty
            } else {
                DefignPalette.get(isDark).primary.asProperty
            }
        }

    private val thumbPosition: XProperty
        get() = if (value) (80.percent - simpleXProperty(Component::width) / 2) else 10.percent

    private val track = RectangleComponent().configure {
        properties {
            width = 100.percent
            height = 100.percent
            radius = 7.px
            fill = trackColor
            stroke = Stroke(DefignPalette.get(isDark).primary, 1.5.px).asProperty
        }
    }.attachTo(this)

    private val thumb = CircleComponent().configure {
        properties {
            x = thumbPosition
            y = CenteredProperty()
            height = 80.percent
            width = simpleWidthProperty { height.getHeight(component) }
            fill = thumbColor
        }
    }.attachTo(track)

    public constructor(
        toggle: Boolean,
        isDark: Boolean = true
    ) : this(stateOf(toggle), isDark)

    init {
        whenMouseClick {
            if (button != 0) {
                return@whenMouseClick
            }

            value = !value
            changeListeners.forEach { listener -> listener.invoke(value) }

            track.animate {
                animateFill(
                    easing = Easings.IN_OUT_QUAD,
                    duration = 200.millis,
                    newValue = trackColor
                )
            }

            thumb.animate {
                animateFill(
                    easing = Easings.IN_OUT_QUAD,
                    duration = 200.millis,
                    newValue = thumbColor
                )

                animateX(
                    easing = Easings.IN_OUT_QUAD,
                    duration = 200.millis,
                    newValue = thumbPosition
                )
            }
        }
    }

    public fun <T : ToggleComponent> onChange(listener: Consumer<Boolean>): T = apply {
        changeListeners.add(listener::accept)
    } as T

}
