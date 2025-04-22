package dev.deftu.componency.components.debugging

import dev.deftu.componency.components.Component

public class DebugSectionBuilder {

    internal val items = mutableListOf<DebugItem>()

    public fun line(text: String) {
        items += DebugLine(text)
    }

    public fun property(name: String, value: Any?) {
        items += DebugProperty(name, value?.toString() ?: "null")
    }

    public fun <T> tree(root: T, children: (T) -> List<T>, render: (T) -> String) {
        items += DebugTree(root, children, render)
    }

    public fun tree(root: Component<*, *>, render: (Component<*, *>) -> String) {
        tree(root, { it.getChildren() }, render)
    }

}
