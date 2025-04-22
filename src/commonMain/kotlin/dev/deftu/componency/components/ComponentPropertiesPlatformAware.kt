package dev.deftu.componency.components

public expect interface ComponentPropertiesPlatformAware<T : Component<T, C>, C : ComponentProperties<T, C>> {

    public val self: C

}
