package xyz.deftu.componency.utils

import net.minecraft.client.MinecraftClient
import kotlin.math.sign

val scaleFactor: Float
    get() = MinecraftClient.getInstance().window.scaleFactor.toFloat()

fun Float.roundToScaledPixels(): Float {
    return (this * scaleFactor).let {
        if (it == 0f && this != 0f) sign(this) else it
    } / scaleFactor
}

fun Double.roundToScaledPixels(): Double {
    return (this * scaleFactor).let {
        if (it == 0.0 && this != 0.0) sign(this) else it
    } / scaleFactor
}
