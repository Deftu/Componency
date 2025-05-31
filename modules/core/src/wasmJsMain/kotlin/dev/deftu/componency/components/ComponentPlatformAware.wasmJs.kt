package dev.deftu.componency.components

public actual interface ComponentPlatformAware<T : Component<T, C>, C : ComponentProperties<T, C>> {

    public actual val self: T

}
