@file:Suppress("FunctionName")

package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.textile.TextHolder
import org.intellij.lang.annotations.Pattern

public open class TextComponentProperties(component: TextComponent) : ComponentProperties<TextComponent, TextComponentProperties>(component) {

    public var text: TextHolder<*, *>? = null

    public var mode: TextComponent.Mode = TextComponent.Mode.NORMAL

}

public open class TextComponent() : Component<TextComponent, TextComponentProperties>(::TextComponentProperties) {

    public enum class Mode {
        NORMAL,
        WRAP
    }

    public data class Line(val text: String, val width: Float)

    public val lines: List<Line>
        get() = when (properties.mode) {
            Mode.NORMAL -> {
                // Only accounts for newlines, otherwhise it will be a single line

                val font = properties.font ?: throw IllegalStateException("Cannot render text without a font")
                val fontSize = properties.fontSize.getFontSize(this)
                val platform = findPlatform(this)

                val lines = properties.text?.asUnformattedString()?.split("\n") ?: listOf()
                val result = mutableListOf<Line>()

                for (line in lines) {
                    val (width, _) = platform.renderer.textRenderer.textSize(font, line, fontSize)
                    result.add(Line(line, width))
                }

                result
            }

            Mode.WRAP -> {
                // Accounts for newlines and wraps text

                val font = properties.font ?: throw IllegalStateException("Cannot render text without a font")
                val fontSize = properties.fontSize.getFontSize(this)
                val platform = findPlatform(this)

                val lines = properties.text?.asUnformattedString()?.split("\n") ?: listOf()
                val width = this.width
                val result = mutableListOf<Line>()

                for (line in lines) {
                    val words = line.split(" ")
                    val lines = mutableListOf<String>()
                    var currentLine = ""
                    var currentWidth = 0f

                    for (word in words) {
                        val (wordWidth, _) = platform.renderer.textRenderer.textSize(font, word, fontSize)

                        if (currentWidth + wordWidth > width) {
                            lines.add(currentLine)
                            currentLine = ""
                            currentWidth = 0f
                        }

                        currentLine += "$word "
                        currentWidth += wordWidth
                    }

                    lines.add(currentLine)
                    result.addAll(lines.map { Line(it, platform.renderer.textRenderer.textSize(font, it, fontSize).width) })
                }

                result
            }
        }

    override fun initialize() {
        val font = properties.font ?: throw IllegalStateException("Cannot render text without a font")
        val text = properties.text ?: throw IllegalStateException("Cannot render text without the text")
        val fontSize = properties.fontSize.getFontSize(this)
        val platform = findPlatform(this)

        if (properties.mode == Mode.NORMAL) {
            val (width, height) = platform.renderer.textRenderer.textSize(font, text.asExclusiveString(), fontSize)
            this.width = width
            this.height = height
        } else {
            val height = lines.fold(0f) { acc, line ->
                val (_, lineHeight) = platform.renderer.textRenderer.textSize(font, line.text, fontSize)
                acc + lineHeight + font.lineSpacing
            }

            this.height = height
        }
    }

    override fun render() {
        val font = properties.font ?: throw IllegalStateException("Cannot render text without a font")
        val fill = properties.fill.getColor(this)
        val fontSize = properties.fontSize.getFontSize(this)

        val platform = findPlatform(this)

        var y = this.top
        for ((index, line) in lines.withIndex()) {
            val text = line.text

            platform.renderer.textRenderer.text(
                x = this.left,
                y = y,
                text = text,
                color = fill,
                font = font,
                fontSize = fontSize
            )

            y += platform.renderer.textRenderer.textSize(font, text, fontSize).height
            if (index != lines.size - 1) {
                y += font.lineSpacing
            }
        }
    }

}

public fun Text(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: TextComponentProperties.() -> Unit = {}
): TextComponent {
    val component = TextComponent()
    component.properties.name = name
    component.properties.block()
    return component
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.Text(
    @Pattern(ComponentProperties.NAME_REGEX)
    name: String? = null,
    block: TextComponentProperties.() -> Unit = {}
): T {
    val component = TextComponent()
    component.properties.name = name
    component.properties.block()
    component.attachTo(this@Text.component)
    return component as T
}
