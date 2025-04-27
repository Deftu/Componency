package dev.deftu.componency.components.debugging

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.font.Font
import dev.deftu.componency.platform.Platform
import kotlin.math.max

public fun renderDebuggerOverlay(
    platform: Platform,
    blocks: List<DebugBlock>,
    font: Font,
    x: Float,
    y: Float,
    fontSize: Float,
    radius: Float,
    padding: Float,
    backgroundColor: Color,
    onHoverState: ((Boolean) -> Unit)? = null,
    onTreeNodeHover: ((Component<*, *>, Float, Float, Float, Float) -> Unit)? = null
) {
    var yOffset = y
    var maxWidth = 0f
    val indentX = 20f

    fun performanceColor(name: String, value: String): Color {
        return when (name.lowercase()) {
            "fps" -> Color.WHITE
            "frame time", "delta time" -> {
                val ms = value.removeSuffix(" ms").toFloatOrNull() ?: return Color.WHITE
                when {
                    ms > 66.66f -> Color.RED
                    ms > 33.33f -> Color.ORANGE
                    else -> Color.WHITE
                }
            }
            else -> Color.WHITE
        }
    }

    for ((index, block) in blocks.withIndex()) {
        val items = when (block) {
            is DebugSection -> block.items
        }

        val titleHeight = platform.renderer.textRenderer.textSize(font, block.title, fontSize).height
        yOffset += padding
        platform.renderer.textRenderer.text(font, block.title, x, yOffset, Color.WHITE, fontSize)
        yOffset += titleHeight + padding

        for (item in items) {
            when (item) {
                is DebugLine -> {
                    val size = platform.renderer.textRenderer.textSize(font, item.text, fontSize)
                    platform.renderer.textRenderer.text(font, item.text, x + indentX, yOffset, Color.WHITE, fontSize)
                    yOffset += size.height + padding
                    maxWidth = max(maxWidth, indentX + size.width)
                }

                is DebugProperty -> {
                    val text = "${item.name}: ${item.value}"
                    val color = performanceColor(item.name, item.value)
                    val size = platform.renderer.textRenderer.textSize(font, text, fontSize)
                    platform.renderer.textRenderer.text(font, text, x + indentX, yOffset, color, fontSize)
                    yOffset += size.height + padding
                    maxWidth = max(maxWidth, indentX + size.width)
                }

                is DebugTree<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    fun <T> renderTree(tree: DebugTree<T>, node: T, depth: Int): Float {
                        val line = tree.renderLine(node)
                        val textX = x + indentX + depth * 20f
                        val size = platform.renderer.textRenderer.textSize(font, line, fontSize)
                        platform.renderer.textRenderer.text(font, line, textX, yOffset, Color.WHITE, fontSize)

                        if (onTreeNodeHover != null && node is Component<*, *>) {
                            val pointerX = platform.inputHandler.pointerInput.pointerX
                            val pointerY = platform.inputHandler.pointerInput.pointerY
                            val width = size.width
                            val height = size.height
                            if (pointerX in textX..(textX + width) && pointerY in yOffset..(yOffset + height)) {
                                onTreeNodeHover(node, textX, yOffset, width, height)
                            }
                        }

                        yOffset += size.height + padding
                        maxWidth = max(maxWidth, textX + size.width)
                        for (child in tree.children(node)) {
                            renderTree(tree, child, depth + 1)
                        }

                        return size.height
                    }

                    @Suppress("UNCHECKED_CAST")
                    renderTree(item as DebugTree<Any?>, item.root, 0)
                }
            }
        }

        if (index < blocks.lastIndex) {
            val dividerHeight = 2f
            val dividerWidth = 150f
            val centerX = x + maxWidth / 2f
            platform.renderer.fill(
                x1 = centerX - dividerWidth / 2f,
                y1 = yOffset,
                x2 = centerX + dividerWidth / 2f,
                y2 = yOffset + dividerHeight,
                color = Color.GRAY.withAlphaPercentage(0.5f),
                radius = 0f
            )
            yOffset += dividerHeight + padding
        }
    }

    val backgroundWidth = maxWidth + padding * 2

    val x1 = x - radius
    val y1 = y - radius
    val x2 = x + backgroundWidth + radius
    val y2 = yOffset + radius

    if (onHoverState != null) {
        val pointerX = platform.inputHandler.pointerInput.pointerX
        val pointerY = platform.inputHandler.pointerInput.pointerY
        onHoverState(pointerX in x1..x2 && pointerY in y1..y2)
    }

    platform.renderer.fill(
        x1 = x1,
        y1 = y1,
        x2 = x2,
        y2 = y2,
        color = backgroundColor,
        radius = radius
    )
}
