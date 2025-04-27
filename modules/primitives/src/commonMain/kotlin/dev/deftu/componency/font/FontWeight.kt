package dev.deftu.componency.font

public enum class FontWeight(
    public val value: Int,
    public val fallback: FontWeight? = null
) {

    REGULAR(400),
    LIGHT(300, REGULAR),
    BOLD(700),

    EXTRA_LIGHT(200, LIGHT),
    THIN(100, EXTRA_LIGHT),
    MEDIUM(500, REGULAR),

    SEMI_BOLD(600, BOLD),
    EXTRA_BOLD(800, BOLD),
    BLACK(900, BOLD);

    public companion object {

        @Suppress("EnumValuesSoftDeprecate")
        public fun from(value: Int): FontWeight {
            return values().firstOrNull { weight ->
                weight.value == value
            } ?: REGULAR
        }

    }

}
