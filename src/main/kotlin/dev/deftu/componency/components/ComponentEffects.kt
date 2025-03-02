package dev.deftu.componency.components

import dev.deftu.componency.effects.Effect
import dev.deftu.componency.utils.Animateable
import dev.deftu.componency.utils.Recalculable

public open class ComponentEffects(public val component: Component) : Animateable, Recalculable {

    public val effects: MutableList<Effect> = mutableListOf()

    override fun frame() {
        for (effect in effects) {
            effect.frame()
        }
    }

    override fun recalculate() {
        for (effect in effects) {
            effect.recalculate()
        }
    }

    public fun add(effect: Effect) {
        effects.add(effect)
    }

    public operator fun plusAssign(effect: Effect) {
        add(effect)
    }

    public operator fun Effect.unaryPlus() {
        add(this)
    }

    public fun remove(effect: Effect) {
        effects.remove(effect)
    }

    public operator fun minusAssign(effect: Effect) {
        remove(effect)
    }

    public operator fun Effect.unaryMinus() {
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
