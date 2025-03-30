package dev.deftu.componency.dsl

import dev.deftu.componency.components.Component
import dev.deftu.componency.effects.Effect

public fun <T : Component> T.effect(effect: Effect): T = apply {
    addEffect(effect)
}

public fun <T : Component> T.effectRemove(effect: Effect): T = apply {
    removeEffect(effect)
}
