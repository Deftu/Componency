package dev.deftu.componency.dsl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentEffects
import dev.deftu.componency.components.ComponentProperties

public class ConfigurationScope<T : Component>(public val component: T) {

    public var name: String?
        get() = component.config.name
        set(value) { component.config.name = value }

    public var hidden: Boolean
        get() = component.config.hidden
        set(value) { component.config.hidden = value }

    public var clipping: Boolean
        get() = component.config.clipping
        set(value) { component.config.clipping = value }

    public fun properties(scope: ComponentProperties.() -> Unit): ComponentProperties {
        return component.config.properties.apply(scope)
    }

    public fun effects(scope: ComponentEffects.() -> Unit): ComponentEffects {
        return component.config.effects.apply(scope)
    }

}

public fun <T : Component> T.attachTo(parent: Component): T = apply {
    parent.addChild(this)
}

public fun <T : Component> T.configure(scope: ConfigurationScope<T>.() -> Unit): T = apply {
    scope(ConfigurationScope(this))
}
