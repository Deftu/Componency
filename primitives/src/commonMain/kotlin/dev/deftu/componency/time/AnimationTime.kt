package dev.deftu.componency.time

import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

public class AnimationTime(
    public val unit: TimeUnit,
    public val value: Double
) {

    public companion object {

        @JvmStatic
        public val ZERO: AnimationTime = AnimationTime(TimeUnit.MILLISECONDS, 0.0)

    }

    public val millis: Double
        @JvmName("toMillis")
        get() = unit.toMillis(value)

}
