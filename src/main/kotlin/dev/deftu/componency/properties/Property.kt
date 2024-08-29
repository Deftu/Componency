package dev.deftu.componency.properties

import dev.deftu.componency.utils.Animateable

public interface Property<T> : Animateable {

    public var cachedValue: T
    public var needsRecalculate: Boolean

    override fun frame() {
        this.needsRecalculate = true
    }

}
