package dev.deftu.componency.minecraft

import dev.deftu.componency.engine.InputEngine
import dev.deftu.omnicore.client.OmniMouse
import dev.deftu.omnicore.client.render.OmniResolution

public object MinecraftInputEngine : InputEngine {

    override val mouseX: Float
        get() = OmniMouse.scaledX.toFloat()

    override val mouseY: Float
        get() = OmniMouse.scaledY.toFloat()

}
