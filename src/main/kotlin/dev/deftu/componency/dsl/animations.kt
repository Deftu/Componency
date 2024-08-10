package dev.deftu.componency.dsl

import dev.deftu.componency.animations.AnimationTime
import dev.deftu.componency.animations.ComponentAnimationProperties
import dev.deftu.componency.components.Component
import java.util.concurrent.TimeUnit

public val Number.millis: AnimationTime
    get() = AnimationTime(TimeUnit.MILLISECONDS, this.toLong())

public val Number.seconds: AnimationTime
    get() = AnimationTime(TimeUnit.SECONDS, this.toLong())

public val Number.minutes: AnimationTime
    get() = AnimationTime(TimeUnit.MINUTES, this.toLong())

public fun <T : Component> T.animate(scope: ComponentAnimationProperties.() -> Unit): T = apply {
    val properties = beginAnimation()
    scope(properties)
}
