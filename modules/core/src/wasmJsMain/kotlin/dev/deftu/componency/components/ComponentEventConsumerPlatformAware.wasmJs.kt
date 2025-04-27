package dev.deftu.componency.components

public actual interface ComponentEventConsumerPlatformAware<T : ComponentEventConsumer<T>> {

    public actual val self: T

}
