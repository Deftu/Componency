package dev.deftu.componency.properties.impl

import dev.deftu.componency.color.Color
import dev.deftu.componency.components.Component
import dev.deftu.componency.dsl.second
import dev.deftu.componency.properties.ColorProperty
import dev.deftu.componency.time.AnimationTime
import dev.deftu.stateful.State
import dev.deftu.stateful.utils.mappedMutableStateOf
import dev.deftu.stateful.utils.mutableStateBound
import dev.deftu.stateful.utils.stateOf

public class RainbowColorProperty(
    cycleDurationState: State<AnimationTime>,
    saturationState: State<Float>,
    brightnessState: State<Float>
) : ColorProperty {

    override var cachedValue: Color = Color.WHITE
    override var needsRecalculate: Boolean = true

    private val cycleDurationState = mappedMutableStateOf(cycleDurationState) { it }
    private val saturationState = mappedMutableStateOf(saturationState) { it }
    private val brightnessState = mappedMutableStateOf(brightnessState) { it }

    public var cycleDuration: AnimationTime by mutableStateBound(this.cycleDurationState)
    public var saturation: Float by mutableStateBound(this.saturationState)
    public var brightness: Float by mutableStateBound(this.brightnessState)

    @JvmOverloads
    public constructor(
        cycleDuration: AnimationTime = 1.second,
        saturation: Float = 1f,
        brightness: Float = 1f
    ) : this(
        stateOf(cycleDuration),
        stateOf(saturation),
        stateOf(brightness)
    )

    override fun calculateColor(component: Component<*, *>): Color {
        val cycleDuration = cycleDuration.millis.toFloat()
        return Color.hsb((System.currentTimeMillis() % cycleDuration) / cycleDuration, saturation, brightness)
    }

}
