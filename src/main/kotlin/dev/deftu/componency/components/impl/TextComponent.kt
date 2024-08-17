package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.engine.Engine
import dev.deftu.textile.SimpleTextHolder
import dev.deftu.textile.TextHolder

public class TextComponent(
    public val text: TextHolder
) : Component() {

    public constructor(text: String) : this(SimpleTextHolder(text))

    override fun initialize() {
        val font = config.properties.font ?: throw IllegalStateException("Cannot render text without a font")
        val fontSize = config.properties.fontSize.getFontSize(this)
        val engine = Engine.get(this)

        val (width, height) = engine.renderEngine.textSize(font, text, fontSize)
        this.width = width
        this.height = height
    }

    override fun render() {
        val font = config.properties.font ?: throw IllegalStateException("Cannot render text without a font")

        val engine = Engine.get(this)

        val fill = config.properties.fill.getColor(this)
        val fontSize = config.properties.fontSize.getFontSize(this)

        engine.renderEngine.text(
            x = left,
            y = top,
            text = text,
            color = fill,
            font = font,
            fontSize = fontSize
        )
    }

}
