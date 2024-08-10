package dev.deftu.componency.components

import dev.deftu.componency.animations.ComponentAnimationProperties
import dev.deftu.componency.utils.Animateable

public open class ComponentConfiguration(private val component: Component) : Animateable {

    public var name: String? = null

    public var hidden: Boolean = false

    public var clipping: Boolean = false

    public var properties: ComponentProperties = ComponentProperties(component)

    public var effects: ComponentEffects = ComponentEffects(component)

    public override fun frame() {
        properties.frame()
        effects.frame()
    }

    public fun beginAnimation(): ComponentAnimationProperties {
        val animation = ComponentAnimationProperties(component)
        animation.copyFrom(this.properties)
        this.properties = animation
        return animation
    }

}
