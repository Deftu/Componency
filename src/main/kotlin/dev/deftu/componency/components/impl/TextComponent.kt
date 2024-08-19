package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.engine.Engine
import dev.deftu.textile.SimpleTextHolder
import dev.deftu.textile.TextHolder

public open class TextComponent @JvmOverloads constructor(
    public val text: TextHolder,
    public val mode: Mode = Mode.NORMAL
) : Component() {

    public enum class Mode {
        NORMAL,
        WRAP
    }

    public data class Line(val text: String, val width: Float)

    @JvmOverloads
    public constructor(text: String, mode: Mode = Mode.NORMAL) : this(SimpleTextHolder(text), mode)

    public val lines: List<Line>
        get() = when (mode) {
            Mode.NORMAL -> {
                // Only accounts for newlines, otherwhise it will be a single line

                val font = this.config.properties.font ?: throw IllegalStateException("Cannot render text without a font")
                val fontSize = this.config.properties.fontSize.getFontSize(this)
                val engine = Engine.get(this)

                val lines = text.asUnformattedString().split("\n")
                val result = mutableListOf<Line>()

                for (line in lines) {
                    val (width, _) = engine.renderEngine.textSize(font, SimpleTextHolder(line), fontSize)
                    result.add(Line(line, width))
                }

                result
            }

            Mode.WRAP -> {
                // Accounts for newlines and wraps text

                val font = this.config.properties.font ?: throw IllegalStateException("Cannot render text without a font")
                val fontSize = this.config.properties.fontSize.getFontSize(this)
                val engine = Engine.get(this)

                val lines = text.asUnformattedString().split("\n")
                val width = this.width
                val result = mutableListOf<Line>()

                for (line in lines) {
                    val words = line.split(" ")
                    val lines = mutableListOf<String>()
                    var currentLine = ""
                    var currentWidth = 0f

                    for (word in words) {
                        val (wordWidth, _) = engine.renderEngine.textSize(font, SimpleTextHolder(word), fontSize)

                        if (currentWidth + wordWidth > width) {
                            lines.add(currentLine)
                            currentLine = ""
                            currentWidth = 0f
                        }

                        currentLine += "$word "
                        currentWidth += wordWidth
                    }

                    lines.add(currentLine)
                    result.addAll(lines.map { Line(it, engine.renderEngine.textSize(font, SimpleTextHolder(it), fontSize).first) })
                }

                result
            }
        }

    override fun initialize() {
        val font = this.config.properties.font ?: throw IllegalStateException("Cannot render text without a font")
        val fontSize = this.config.properties.fontSize.getFontSize(this)
        val engine = Engine.get(this)

        if (mode == Mode.NORMAL) {
            val (width, height) = engine.renderEngine.textSize(font, text, fontSize)
            this.width = width
            this.height = height
        } else {
            val height = lines.fold(0f) { acc, line ->
                val (_, lineHeight) = engine.renderEngine.textSize(font, SimpleTextHolder(line.text), fontSize)
                acc + lineHeight + font.lineSpacing
            }

            this.height = height
        }
    }

    override fun render() {
        val font = this.config.properties.font ?: throw IllegalStateException("Cannot render text without a font")
        val fill = this.config.properties.fill.getColor(this)
        val fontSize = this.config.properties.fontSize.getFontSize(this)

        val engine = Engine.get(this)

        var y = this.top
        for ((index, line) in lines.withIndex()) {
            val text = SimpleTextHolder(line.text)

            engine.renderEngine.text(
                x = this.left,
                y = y,
                text = text,
                color = fill,
                font = font,
                fontSize = fontSize
            )

            y += engine.renderEngine.textSize(font, text, fontSize).second
            if (index != lines.size - 1) {
                y += font.lineSpacing
            }
        }
    }

}
