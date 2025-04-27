package dev.deftu.componency.platform.input

import dev.deftu.componency.input.Key

public interface KeyboardInput {

    public val isShiftKeyDown: Boolean

    public val isCtrlKeyDown: Boolean

    public val isAltKeyDown: Boolean

    public val isSuperKeyDown: Boolean

    public val isCapsLockEnabled: Boolean

    public val isNumLockEnabled: Boolean

    public fun isKeyDown(key: Key): Boolean

    public fun setKeyDown(key: Key, isDown: Boolean)

}
