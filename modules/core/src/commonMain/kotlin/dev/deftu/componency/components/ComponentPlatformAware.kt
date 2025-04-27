package dev.deftu.componency.components

public expect interface ComponentPlatformAware<T : Component<T, C>, C : ComponentProperties<T, C>> {

    public val self: T

}
