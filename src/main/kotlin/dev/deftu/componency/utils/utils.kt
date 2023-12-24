package dev.deftu.componency.utils

import dev.deftu.multi.MultiResolution

private val halfPixel = 0.5 / MultiResolution.scaleFactor

fun fixMousePos(x: Double, y: Double): Pair<Double, Double> {
    return (x + halfPixel) to (y + halfPixel)
}
