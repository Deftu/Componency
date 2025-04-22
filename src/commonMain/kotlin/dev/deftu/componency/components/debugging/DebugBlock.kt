package dev.deftu.componency.components.debugging

public sealed interface DebugBlock

public data class DebugSection(val title: String, val items: List<DebugItem>) : DebugBlock
