package dev.deftu.componency.components

public class SimpleComponent<C : ComponentProperties<SimpleComponent<C>, C>>(propertiesFactory: (SimpleComponent<C>) -> C) : Component<SimpleComponent<C>, C>(propertiesFactory)
