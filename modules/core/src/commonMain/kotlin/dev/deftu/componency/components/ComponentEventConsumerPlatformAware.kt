package dev.deftu.componency.components

public expect interface ComponentEventConsumerPlatformAware<T : ComponentEventConsumer<T>> {

    public val self: T

}
