package dev.deftu.componency.components.debugging

public class DebugOverlayBuilder {

    private val blocks = mutableListOf<DebugBlock>()

    public fun section(title: String, builder: DebugSectionBuilder.() -> Unit) {
        blocks += DebugSection(title, DebugSectionBuilder().apply(builder).items)
    }

    public fun build(): List<DebugBlock> {
        return blocks
    }

}
