package dev.deftu.componency.components

import dev.deftu.componency.effects.Effect

public open class ComponentEffects<T : Component<T, C>, C : ComponentProperties<T, C>>(
    public val component: Component<T, C>
) {

    public val effects: MutableList<Effect<T, C>> = mutableListOf()

    public open fun animationFrame(deltaTime: Float) {
        for (effect in effects) {
            effect.animationFrame(deltaTime)
        }
    }

    public open fun recalculate() {
        for (effect in effects) {
            effect.recalculate()
        }
    }

    public fun add(effect: Effect<T, C>) {
        effects.add(effect)
    }

    public operator fun plusAssign(effect: Effect<T, C>) {
        add(effect)
    }

    public operator fun Effect<T, C>.unaryPlus() {
        add(this)
    }

    public fun remove(effect: Effect<T, C>) {
        effects.remove(effect)
    }

    public operator fun minusAssign(effect: Effect<T, C>) {
        remove(effect)
    }

    public operator fun Effect<T, C>.unaryMinus() {
        remove(this)
    }

    public fun preRender() {
        for (effect in effects) {
            effect.preRender(component)
        }
    }

    public fun postRender() {
        for (effect in effects) {
            effect.postRender(component)
        }
    }

}
