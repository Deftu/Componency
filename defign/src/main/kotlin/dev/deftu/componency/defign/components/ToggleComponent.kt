@file:Suppress("UNCHECKED_CAST", "LeakingThis", "FunctionName")

package dev.deftu.componency.defign.components

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.components.SimpleComponentProperties
import dev.deftu.componency.components.impl.Circle
import dev.deftu.componency.components.impl.FrameComponent
import dev.deftu.componency.components.impl.Rectangle
import dev.deftu.componency.components.traits.focusable
import dev.deftu.componency.components.traits.setFocusableDisabled
import dev.deftu.componency.defign.DefignPalette
import dev.deftu.componency.dsl.*
import dev.deftu.componency.easings.Easings
import dev.deftu.componency.input.MouseButton
import dev.deftu.componency.properties.ColorProperty
import dev.deftu.componency.properties.XProperty
import dev.deftu.componency.stroke.Stroke
import dev.deftu.stateful.MutableState
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.mutableStateOf
import org.intellij.lang.annotations.Pattern
import java.util.function.Consumer

public class ToggleComponentProperties(private val _component: ToggleComponent) : DefignComponentProperties<ToggleComponent, ToggleComponentProperties>(_component) {

    public var value: MutableState<Boolean> = mutableStateOf(false)

    public var isDisabled: Boolean = false
        set(value) {
            _component.setFocusableDisabled(value)
            field = value
        }

}

public open class ToggleComponent() : Component<ToggleComponent, ToggleComponentProperties>(::ToggleComponentProperties) {

    public companion object {
        public const val DEFAULT_TOGGLE_WIDTH: Float = 32.5f
        public const val DEFAULT_TOGGLE_HEIGHT: Float = 15f
    }
    
    private val trackColor: ColorProperty
        get() = if (properties.isDark.get()) {
            DefignPalette.get(properties.isDark.get()).primary.asProperty
        } else {
            DefignPalette.get(properties.isDark.get()).background2.asProperty
        }

    private val thumbColor: ColorProperty
        get() = if (properties.isDark.get()) {
            DefignPalette.get(properties.isDark.get()).background2.asProperty
        } else {
            DefignPalette.get(properties.isDark.get()).primary.asProperty
        }

    public val changeListeners: MutableList<(Boolean) -> Unit> = mutableListOf()

    public var value: Boolean by mutableStateBound(properties.value)

    private val thumbPosition: XProperty
        get() = if (value) (80.percent - simpleXProperty(Component<*, *>::width) / 2) else 10.percent

    private val track = Rectangle("track") {
        size(100.percent, 100.percent)
        radius = 10.px
        fill = trackColor
        stroke = Stroke(DefignPalette.get(properties.isDark.get()).primary, 1.5f).asProperty
    }.attachTo(this)

    private val thumb = Circle("thumb") {
        position(thumbPosition, centered)
        size(80.percent, 80.percent)
        fill = thumbColor
    }.attachTo(track)

    init {
        focusable()
        onPointerClick { event ->
            if (event.button != MouseButton.LEFT) {
                return@onPointerClick
            }

            value = !value
            changeListeners.forEach { listener -> listener.invoke(value) }

            track.properties.animateFill(Easings.IN_OUT_QUAD, 200.millis, trackColor)
            thumb.properties.animateFill(Easings.IN_OUT_QUAD, 200.millis, thumbColor)
            thumb.properties.animateX(Easings.IN_OUT_QUAD, 200.millis, thumbPosition)
        }
    }

    public fun <T : ToggleComponent> onChange(listener: Consumer<Boolean>): T = apply {
        changeListeners.add(listener::accept)
    } as T

}

public fun Toggle(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: ToggleComponentProperties.() -> Unit = {}
): ToggleComponent {
    val component = ToggleComponent()
    component.properties.name = name
    component.properties.block()
    return component
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Toggle(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: ToggleComponentProperties.() -> Unit = {}
): T {
    val component = ToggleComponent()
    component.properties.name = name
    component.properties.block()
    component.attachTo(this@Toggle.component)
    return component as T
}
