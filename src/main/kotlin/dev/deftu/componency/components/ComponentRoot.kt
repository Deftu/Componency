package dev.deftu.componency.components

import dev.deftu.componency.platform.Platform

public interface ComponentRoot<T : ComponentRoot<T>> {

    public val isRoot: Boolean

    public val platform: Platform?

    public fun makeRoot(platform: Platform): T

}
