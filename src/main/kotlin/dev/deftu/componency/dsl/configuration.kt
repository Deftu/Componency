package dev.deftu.componency.dsl

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.platform.Platform
import dev.deftu.componency.properties.*

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.root(platform: Platform): C = apply {
    this.component.makeRoot(platform)
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.debugger(): C = apply {
    this.component.enableDebugger()
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.position(x: XProperty, y: YProperty): C = apply {
    this.x = x
    this.y = y
}

public fun <T : Component<T, C>, C : ComponentProperties<T, C>> C.size(width: WidthProperty, height: HeightProperty): C = apply {
    this.width = width
    this.height = height
}
