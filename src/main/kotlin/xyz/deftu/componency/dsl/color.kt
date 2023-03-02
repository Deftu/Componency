package xyz.deftu.componency.dsl

import java.awt.Color

fun Color.withRed(red: Int) = Color(red, green, blue, alpha)
fun Color.withGreen(green: Int) = Color(red, green, blue, alpha)
fun Color.withBlue(blue: Int) = Color(red, green, blue, alpha)
fun Color.withAlpha(alpha: Int) = Color(red, green, blue, alpha)
