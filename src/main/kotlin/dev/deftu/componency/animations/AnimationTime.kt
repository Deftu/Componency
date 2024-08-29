package dev.deftu.componency.animations

public class AnimationTime(
    public val unit: TimeUnit,
    public val value: Double
) {

    public companion object {

        @JvmStatic
        public val ZERO: AnimationTime = AnimationTime(TimeUnit.MILLISECONDS, 0.0)

    }

}
