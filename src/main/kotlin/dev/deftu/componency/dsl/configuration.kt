package dev.deftu.componency.dsl

import dev.deftu.componency.animations.ComponentAnimationConstraints
import dev.deftu.componency.components.BaseComponent
import dev.deftu.componency.constraints.ComponentConstraints
import dev.deftu.componency.effects.Effect
import dev.deftu.componency.filters.Filter

fun <T : BaseComponent> T.configure(scope: ConfigurationScope<T>.() -> Unit) = apply {
    scope(ConfigurationScope(this))
}

fun <T : BaseComponent> T.animate(block: ComponentAnimationConstraints.() -> Unit) = apply {
    val constraints = ComponentAnimationConstraints(this, constraints)
    constraints.block()
    this.constraints = constraints
}

class ConfigurationScope<T : BaseComponent>(
    val component: T
) {
    val filters: MutableList<Filter>
        get() = component.filters
    val effects: MutableList<Effect>
        get() = component.effects

    fun constraints(scope: ComponentConstraints.() -> Unit) = apply {
        component.constraints.apply(scope)
    }
}
