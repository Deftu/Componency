package dev.deftu.componency.components.events

import dev.deftu.componency.components.Component
import dev.deftu.componency.components.ComponentProperties
import dev.deftu.componency.input.Key
import kotlin.jvm.JvmOverloads

public data class KeyboardModifiers @JvmOverloads constructor(
    public val isShift: Boolean,
    public val isCtrl: Boolean,
    public val isAlt: Boolean,
    public val isSuper: Boolean,
    public val isCapsLock: Boolean = false,
    public val isNumLock: Boolean = false,
)

public data class KeyPressEvent(
    override val component: Component<*, *>,
    public val key: Key,
    public val modifiers: KeyboardModifiers,
) : CancellableComponentEvent(component)

public data class KeyReleaseEvent(
    override val component: Component<*, *>,
    public val key: Key,
    public val modifiers: KeyboardModifiers,
) : CancellableComponentEvent(component)

public data class CharTypeEvent(
    override val component: Component<*, *>,
    public val char: Char,
    public val modifiers: KeyboardModifiers,
) : CancellableComponentEvent(component)
