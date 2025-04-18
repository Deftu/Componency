package dev.deftu.componency.properties

public interface Property<T> {

    public var cachedValue: T
    public var needsRecalculate: Boolean

    public fun animationFrame(deltaTime: Float) {
        this.needsRecalculate = true
    }

}
