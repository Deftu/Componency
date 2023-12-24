package dev.deftu.componency.animations

import kotlin.math.*

interface AnimationHandler {
    fun getValue(progress: Float): Float
}

/**
 * Implementations of basic easing functions for animations.
 *
 * @see <a href="https://easings.net/">https://easings.net/</a>
 */
enum class Animations : AnimationHandler {
    LINEAR {
        override fun getValue(progress: Float): Float {
            return progress
        }
    },
    IN_QUAD {
        override fun getValue(progress: Float): Float {
            return progress * progress
        }
    },
    OUT_QUAD {
        override fun getValue(progress: Float): Float {
            return 1 - (1 - progress) * (1 - progress)
        }
    },
    IN_OUT_QUAD {
        override fun getValue(progress: Float): Float {
            return if (progress < 0.5) {
                2 * progress * progress
            } else {
                1 - (-2 * progress + 2) * (-2 * progress + 2) / 2
            }
        }
    },
    IN_CUBIC {
        override fun getValue(progress: Float): Float {
            return progress * progress * progress
        }
    },
    OUT_CUBIC {
        override fun getValue(progress: Float): Float {
            return 1 - (1 - progress) * (1 - progress) * (1 - progress)
        }
    },
    IN_OUT_CUBIC {
        override fun getValue(progress: Float): Float {
            return if (progress < 0.5) {
                4 * progress * progress * progress
            } else {
                1 - (-2 * progress + 2) * (-2 * progress + 2) * (-2 * progress + 2) / 2
            }
        }
    },
    IN_QUART {
        override fun getValue(progress: Float): Float {
            return progress * progress * progress * progress
        }
    },
    OUT_QUART {
        override fun getValue(progress: Float): Float {
            return 1 - (1 - progress) * (1 - progress) * (1 - progress) * (1 - progress)
        }
    },
    IN_OUT_QUART {
        override fun getValue(progress: Float): Float {
            return if (progress < 0.5) {
                8 * progress * progress * progress * progress
            } else {
                1 - (-2 * progress + 2) * (-2 * progress + 2) * (-2 * progress + 2) * (-2 * progress + 2) / 2
            }
        }
    },
    IN_QUINT {
        override fun getValue(progress: Float): Float {
            return progress * progress * progress * progress * progress
        }
    },
    OUT_QUINT {
        override fun getValue(progress: Float): Float {
            return 1 - (1 - progress) * (1 - progress) * (1 - progress) * (1 - progress) * (1 - progress)
        }
    },
    IN_OUT_QUINT {
        override fun getValue(progress: Float): Float {
            return if (progress < 0.5) {
                16 * progress * progress * progress * progress * progress
            } else {
                1 - (-2 * progress + 2.0).pow(5.0).toFloat() / 2
            }
        }
    },
    IN_SINE {
        override fun getValue(progress: Float): Float {
            return 1 - cos(progress * PI / 2).toFloat()
        }
    },
    OUT_SINE {
        override fun getValue(progress: Float): Float {
            return sin(progress * PI / 2).toFloat()
        }
    },
    IN_OUT_SINE {
        override fun getValue(progress: Float): Float {
            return -(cos(PI * progress).toFloat() - 1) / 2
        }
    },
    IN_EXPO {
        override fun getValue(progress: Float): Float {
            return if (progress == 0f) 0f else 2f.pow(10 * (progress - 1))
        }
    },
    OUT_EXPO {
        override fun getValue(progress: Float): Float {
            return if (progress == 1f) 1f else 1 - 2f.pow(-10 * progress)
        }
    },
    IN_OUT_EXPO {
        override fun getValue(progress: Float): Float {
            return if (progress == 0f) 0f else if (progress == 1f) 1f else if (progress < 0.5) {
                2f.pow(20 * progress - 10) / 2
            } else {
                (2 - 2f.pow(-20 * progress + 10)) / 2
            }
        }
    },
    IN_CIRC {
        override fun getValue(progress: Float): Float {
            return 1 - sqrt(1 - (progress * progress))
        }
    },
    OUT_CIRC {
        override fun getValue(progress: Float): Float {
            return sqrt((2 - progress) * progress)
        }
    },
    IN_OUT_CIRC {
        override fun getValue(progress: Float): Float {
            return if (progress < 0.5) {
                (1 - sqrt(1 - 4 * (progress * progress))) / 2
            } else {
                (sqrt(-((2 * progress) - 3) * ((2 * progress) - 1)) + 1) / 2
            }
        }
    },
    IN_BACK {
        override fun getValue(progress: Float): Float {
            return progress * progress * ((1.70158 + 1) * progress - 1.70158).toFloat()
        }
    },
    OUT_BACK {
        override fun getValue(progress: Float): Float {
            return 1 - (1 - progress) * (1 - progress) * ((1.70158 + 1) * (1 - progress) + 1.70158).toFloat()
        }
    },
    IN_OUT_BACK {
        override fun getValue(progress: Float): Float {
            return if (progress < 0.5) {
                (2 * progress * progress * ((2.5949095 + 1) * 2 * progress - 2.5949095).toFloat()) / 2
            } else {
                (1 - (-2 * progress + 2) * (-2 * progress + 2) * ((2.5949095 + 1) * (-2 * progress + 2) + 2.5949095).toFloat()) / 2
            }
        }
    },
    IN_ELASTIC {
        override fun getValue(progress: Float): Float {
            return when (progress) {
                0f -> 0f
                1f -> 1f
                else -> (-2f).pow(10 * (progress - 1)) * sin((progress - 1.075) * (2 * PI) / 0.3).toFloat()
            }
        }
    },
    OUT_ELASTIC {
        override fun getValue(progress: Float): Float {
            return when (progress) {
                0f -> 0f
                1f -> 1f
                else -> 2f.pow(-10 * progress) * sin((progress - 0.075) * (2 * PI) / 0.3).toFloat() + 1
            }
        }
    },
    IN_OUT_ELASTIC {
        override fun getValue(progress: Float): Float {
            return if (progress == 0f) 0f else if (progress == 1f) 1f else if (progress < 0.5) {
                ((-2f).pow(20 * progress - 10) * sin((20 * progress - 11.125) * (PI) / 4.5)).toFloat() / 2
            } else {
                (2f.pow(-20 * progress + 10) * sin((20 * progress - 11.125) * (PI) / 4.5)).toFloat() / 2 + 1
            }
        }
    },
    IN_BOUNCE {
        override fun getValue(progress: Float): Float {
            return 1 - OUT_BOUNCE.getValue(1 - progress)
        }
    },
    OUT_BOUNCE {
        override fun getValue(progress: Float): Float {
            return if (progress < 4 / 11.0) {
                (121 * progress * progress) / 16f
            } else if (progress < 8 / 11.0) {
                (363 / 40f * progress * progress) - (99 / 10f * progress) + 17 / 5f
            } else if (progress < 9 / 10.0) {
                (4356 / 361f * progress * progress) - (35442 / 1805f * progress) + 16061 / 1805f
            } else {
                (54 / 5f * progress * progress) - (513 / 25f * progress) + 268 / 25f
            }
        }
    },
    IN_OUT_BOUNCE {
        override fun getValue(progress: Float): Float {
            return if (progress < 0.5) {
                (1 - OUT_BOUNCE.getValue(1 - 2 * progress)) / 2
            } else {
                (1 + OUT_BOUNCE.getValue(2 * progress - 1)) / 2
            }
        }
    };
}
