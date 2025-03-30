package dev.deftu.componency.engine

import dev.deftu.componency.components.Component
import dev.deftu.componency.exceptions.InvalidHierarchyException
import dev.deftu.componency.font.Font
import dev.deftu.componency.font.FontFamily
import java.nio.file.Path

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
