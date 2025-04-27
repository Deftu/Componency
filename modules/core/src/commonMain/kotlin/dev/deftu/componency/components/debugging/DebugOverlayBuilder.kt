package dev.deftu.componency.components.debugging

import kotlin.jvm.JvmOverloads

public class DebugOverlayBuilder @JvmOverloads constructor(block: DebugOverlayBuilder.() -> Unit = {}) {

    private val blocks = mutableListOf<DebugBlock>()

    init {
        apply(block)
    }

    public fun section(title: String, builder: DebugSectionBuilder.() -> Unit) {
        blocks += DebugSection(title, DebugSectionBuilder().apply(builder).items)
    }

    public fun build(): List<DebugBlock> {
        return blocks
    }

}
