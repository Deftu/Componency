package xyz.deftu.componency.utils

import xyz.deftu.multi.MultiResolution

private val halfPixel = 0.5 / MultiResolution.scaleFactor

fun fixMousePos(x: Double, y: Double): Pair<Double, Double> {
    return (x + halfPixel) to (y + halfPixel)
}
