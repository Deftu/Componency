package dev.deftu.componency.engine

import dev.deftu.componency.components.Component
import dev.deftu.componency.exceptions.InvalidHierarchyException

public abstract class Engine {

    public companion object {

        @JvmStatic
        public fun get(component: Component): Engine {
            return component.engine ?: Component.findRoot(component).engine ?: throw InvalidHierarchyException("No engine present")
        }

    }

    public abstract val renderEngine: RenderEngine

    public abstract val inputEngine: InputEngine

}
