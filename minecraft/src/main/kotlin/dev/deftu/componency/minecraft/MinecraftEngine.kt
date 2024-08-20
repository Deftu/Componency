package dev.deftu.componency.minecraft

import dev.deftu.componency.engine.Engine

public object MinecraftEngine : Engine() {

    override val renderEngine: MinecraftRenderEngine = MinecraftRenderEngine

    override val inputEngine: MinecraftInputEngine = MinecraftInputEngine

}
