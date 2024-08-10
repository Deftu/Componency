package dev.deftu.componency.animations

import java.util.concurrent.TimeUnit

public class AnimationTime(
    public val unit: TimeUnit,
    public val value: Long
) {

    public companion object {

        @JvmStatic
        public val ZERO: AnimationTime = AnimationTime(TimeUnit.MILLISECONDS, 0)

    }

}
