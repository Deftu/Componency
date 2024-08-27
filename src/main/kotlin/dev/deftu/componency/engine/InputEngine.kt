package dev.deftu.componency.engine

import dev.deftu.componency.input.Key
import dev.deftu.componency.input.MouseButton

public interface InputEngine {

    public val mouseX: Float

    public val mouseY: Float

    public fun isMouseButtonDown(button: MouseButton): Boolean

    public fun isKeyDown(key: Key): Boolean

}
