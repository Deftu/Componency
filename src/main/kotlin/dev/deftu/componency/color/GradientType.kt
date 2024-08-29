package dev.deftu.componency.color

public interface GradientType {

    public data class Linear(public val direction: LinearDirection) : GradientType

    public enum class LinearDirection {
        HORIZONTAL,
        VERTICAL,
        DIAGONAL
    }

    public data class Radial(
        public val innerRadius: Float,
        public val outerRadius: Float,
        public val center: Pair<Float, Float>
    ) : GradientType {

        public constructor(
            innerRadius: Float,
            outerRadius: Float,
            centerX: Float = -1f,
            centerY: Float = -1f
        ) : this(innerRadius, outerRadius, Pair(centerX, centerY))

    }

}
