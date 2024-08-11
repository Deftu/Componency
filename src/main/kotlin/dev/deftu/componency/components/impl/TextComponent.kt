package dev.deftu.componency.components.impl

import dev.deftu.componency.components.Component
import dev.deftu.componency.engine.Engine
import dev.deftu.textile.SimpleTextHolder
import dev.deftu.textile.TextHolder

public class TextComponent(
    public val text: TextHolder
) : Component() {

    public constructor(text: String) : this(SimpleTextHolder(text))

    override fun render() {
        val font = config.properties.font ?: throw IllegalStateException("Cannot render text without a font")

        val engine = Engine.get(this)

        val fill = config.properties.fill.getColor(this)

        engine.renderEngine.text(
            x = left,
            y = top,
            text = text,
            color = fill,
            font = font
        )
    }

}
