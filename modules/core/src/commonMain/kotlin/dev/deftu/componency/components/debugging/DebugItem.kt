package dev.deftu.componency.components.debugging

public sealed interface DebugItem

public data class DebugLine(val text: String) : DebugItem

public data class DebugProperty(val name: String, val value: String) : DebugItem

public data class DebugTree<T>(
    val root: T,
    val children: (T) -> List<T>,
    val renderLine: (T) -> String
) : DebugItem
