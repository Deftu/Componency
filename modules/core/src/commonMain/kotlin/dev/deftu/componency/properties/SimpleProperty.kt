package dev.deftu.componency.properties

public abstract class SimpleProperty<T>(defaultValue: T) : Property<T> {

    override var cachedValue: T = defaultValue
    override var needsRecalculate: Boolean = true

}
