package dev.deftu.componency.canvas

public data class FontRef(
    val family: String,
    val weight: FontWeight = FontWeight.Normal,
    val isItalic: Boolean = false,
    val features: List<FontFeature> = emptyList()
)

public enum class FontWeight(public val value: Int) {
    Thin(100),
    ExtraLight(200),
    Light(300),
    Normal(400),
    Medium(500),
    SemiBold(600),
    Bold(700),
    ExtraBold(800),
    Black(900)
}

public data class FontFeature(val tag: String, val value: Int = 1)
