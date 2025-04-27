package dev.deftu.componency.components

public actual interface ComponentPropertiesPlatformAware<T : Component<T, C>, C : ComponentProperties<T, C>> {

    public actual val self: C

}
