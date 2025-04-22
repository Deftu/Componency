package dev.deftu.componency.dsl

import dev.deftu.componency.time.AnimationTime
import dev.deftu.componency.time.TimeUnit

public val Number.millis: AnimationTime
    get() = AnimationTime(TimeUnit.MILLISECONDS, this.toDouble())

public val Number.milli: AnimationTime
    get() = AnimationTime(TimeUnit.MILLISECONDS, this.toDouble())

public val Number.seconds: AnimationTime
    get() = AnimationTime(TimeUnit.SECONDS, this.toDouble())

public val Number.second: AnimationTime
    get() = AnimationTime(TimeUnit.SECONDS, this.toDouble())

public val Number.minutes: AnimationTime
    get() = AnimationTime(TimeUnit.MINUTES, this.toDouble())

public val Number.minute: AnimationTime
    get() = AnimationTime(TimeUnit.MINUTES, this.toDouble())
