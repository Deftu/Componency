package dev.deftu.componency.animations

import kotlin.math.*

/**
 * Implementations of basic easing functions for animations.
 *
 * @see <a href="https://easings.net/">https://easings.net/</a>
 *
 * @since 0.1.0
 */
public enum class Easings : Easing {

    LINEAR {
        override fun ease(time: Float): Float {
            return time
        }
    },

    IN_QUAD {
        override fun ease(time: Float): Float {
            return time * time
        }
    },

    OUT_QUAD {
        override fun ease(time: Float): Float {
            return 1 - (1 - time) * (1 - time)
        }
    },

    IN_OUT_QUAD {
        override fun ease(time: Float): Float {
            return if (time < 0.5) {
                2 * time * time
            } else {
                1 - (-2 * time + 2) * (-2 * time + 2) / 2
            }
        }
    },

    IN_CUBIC {
        override fun ease(time: Float): Float {
            return time * time * time
        }
    },

    OUT_CUBIC {
        override fun ease(time: Float): Float {
            return 1 - (1 - time) * (1 - time) * (1 - time)
        }
    },

    IN_OUT_CUBIC {
        override fun ease(time: Float): Float {
            return if (time < 0.5) {
                4 * time * time * time
            } else {
                1 - (-2 * time + 2) * (-2 * time + 2) * (-2 * time + 2) / 2
            }
        }
    },

    IN_QUART {
        override fun ease(time: Float): Float {
            return time * time * time * time
        }
    },

    OUT_QUART {
        override fun ease(time: Float): Float {
            return 1 - (1 - time) * (1 - time) * (1 - time) * (1 - time)
        }
    },

    IN_OUT_QUART {
        override fun ease(time: Float): Float {
            return if (time < 0.5) {
                8 * time * time * time * time
            } else {
                1 - (-2 * time + 2) * (-2 * time + 2) * (-2 * time + 2) * (-2 * time + 2) / 2
            }
        }
    },

    IN_QUINT {
        override fun ease(time: Float): Float {
            return time * time * time * time * time
        }
    },

    OUT_QUINT {
        override fun ease(time: Float): Float {
            return 1 - (1 - time) * (1 - time) * (1 - time) * (1 - time) * (1 - time)
        }
    },

    IN_OUT_QUINT {
        override fun ease(time: Float): Float {
            return if (time < 0.5) {
                16 * time * time * time * time * time
            } else {
                1 - (-2 * time + 2.0).pow(5.0).toFloat() / 2
            }
        }
    },

    IN_SINE {
        override fun ease(time: Float): Float {
            return 1 - cos(time * PI / 2).toFloat()
        }
    },

    OUT_SINE {
        override fun ease(time: Float): Float {
            return sin(time * PI / 2).toFloat()
        }
    },

    IN_OUT_SINE {
        override fun ease(time: Float): Float {
            return -(cos(PI * time).toFloat() - 1) / 2
        }
    },

    IN_EXPO {
        override fun ease(time: Float): Float {
            return if (time == 0f) 0f else 2f.pow(10 * (time - 1))
        }
    },

    OUT_EXPO {
        override fun ease(time: Float): Float {
            return if (time == 1f) 1f else 1 - 2f.pow(-10 * time)
        }
    },

    IN_OUT_EXPO {
        override fun ease(time: Float): Float {
            return if (time == 0f) 0f else if (time == 1f) 1f else if (time < 0.5) {
                2f.pow(20 * time - 10) / 2
            } else {
                (2 - 2f.pow(-20 * time + 10)) / 2
            }
        }
    },

    IN_CIRC {
        override fun ease(time: Float): Float {
            return 1 - sqrt(1 - (time * time))
        }
    },

    OUT_CIRC {
        override fun ease(time: Float): Float {
            return sqrt((2 - time) * time)
        }
    },

    IN_OUT_CIRC {
        override fun ease(time: Float): Float {
            return if (time < 0.5) {
                (1 - sqrt(1 - 4 * (time * time))) / 2
            } else {
                (sqrt(-((2 * time) - 3) * ((2 * time) - 1)) + 1) / 2
            }
        }
    },

    IN_BACK {
        override fun ease(time: Float): Float {
            return time * time * ((1.70158 + 1) * time - 1.70158).toFloat()
        }
    },

    OUT_BACK {
        override fun ease(time: Float): Float {
            return 1 - (1 - time) * (1 - time) * ((1.70158 + 1) * (1 - time) + 1.70158).toFloat()
        }
    },

    IN_OUT_BACK {
        override fun ease(time: Float): Float {
            return if (time < 0.5) {
                (2 * time * time * ((2.5949095 + 1) * 2 * time - 2.5949095).toFloat()) / 2
            } else {
                (1 - (-2 * time + 2) * (-2 * time + 2) * ((2.5949095 + 1) * (-2 * time + 2) + 2.5949095).toFloat()) / 2
            }
        }
    },

    IN_ELASTIC {
        override fun ease(time: Float): Float {
            return when (time) {
                0f -> 0f
                1f -> 1f
                else -> (-2f).pow(10 * (time - 1)) * sin((time - 1.075) * (2 * PI) / 0.3).toFloat()
            }
        }
    },

    OUT_ELASTIC {
        override fun ease(time: Float): Float {
            return when (time) {
                0f -> 0f
                1f -> 1f
                else -> 2f.pow(-10 * time) * sin((time - 0.075) * (2 * PI) / 0.3).toFloat() + 1
            }
        }
    },

    IN_OUT_ELASTIC {
        override fun ease(time: Float): Float {
            return if (time == 0f) 0f else if (time == 1f) 1f else if (time < 0.5) {
                ((-2f).pow(20 * time - 10) * sin((20 * time - 11.125) * (PI) / 4.5)).toFloat() / 2
            } else {
                (2f.pow(-20 * time + 10) * sin((20 * time - 11.125) * (PI) / 4.5)).toFloat() / 2 + 1
            }
        }
    },

    IN_BOUNCE {
        override fun ease(time: Float): Float {
            return 1 - OUT_BOUNCE.ease(1 - time)
        }
    },

    OUT_BOUNCE {
        override fun ease(time: Float): Float {
            return if (time < 4 / 11.0) {
                (121 * time * time) / 16f
            } else if (time < 8 / 11.0) {
                (363 / 40f * time * time) - (99 / 10f * time) + 17 / 5f
            } else if (time < 9 / 10.0) {
                (4356 / 361f * time * time) - (35442 / 1805f * time) + 16061 / 1805f
            } else {
                (54 / 5f * time * time) - (513 / 25f * time) + 268 / 25f
            }
        }
    },

    IN_OUT_BOUNCE {
        override fun ease(time: Float): Float {
            return if (time < 0.5) {
                (1 - OUT_BOUNCE.ease(1 - 2 * time)) / 2
            } else {
                (1 + OUT_BOUNCE.ease(2 * time - 1)) / 2
            }
        }
    };

}
