@file:Suppress("FunctionName")

package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.components.SimpleComponentProperties
import org.intellij.lang.annotations.Pattern

public open class FrameComponent : Component<FrameComponent, SimpleComponentProperties<FrameComponent>>(::SimpleComponentProperties)

public fun Frame(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: SimpleComponentProperties<FrameComponent>.() -> Unit = {}
): FrameComponent {
    val component = FrameComponent()
    component.properties.name = name
    component.properties.block()
    return component
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Frame(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: SimpleComponentProperties<FrameComponent>.() -> Unit = {}
): T {
    val component = FrameComponent()
    component.properties.name = name
    component.properties.block()
    component.attachTo(this@Frame.component)
    return component as T
}
