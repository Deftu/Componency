package dev.deftu.componency.minecraft

import dev.deftu.componency.engine.Engine
import dev.deftu.componency.engine.InputEngine
import dev.deftu.componency.engine.RenderEngine

public object MinecraftEngine : Engine() {

    override val renderEngine: RenderEngine = MinecraftRenderEngine

    override val inputEngine: InputEngine = MinecraftInputEngine

}
