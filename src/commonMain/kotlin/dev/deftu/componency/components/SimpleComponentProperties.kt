package dev.deftu.componency.components

public open class SimpleComponentProperties<T : Component<T, SimpleComponentProperties<T>>>(component: Component<T, SimpleComponentProperties<T>>) : ComponentProperties<T, SimpleComponentProperties<T>>(component)
