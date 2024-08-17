package dev.deftu.componency.animations

public enum class TimeUnit {

    MILLISECONDS,
    SECONDS,
    MINUTES,
    HOURS,
    DAYS;

    public fun toMillis(value: Double): Double = when (this) {
        MILLISECONDS -> value
        SECONDS -> value * 1000
        MINUTES -> value * 60000
        HOURS -> value * 3600000
        DAYS -> value * 86400000
    }

    public fun toSeconds(value: Double): Double = when (this) {
        MILLISECONDS -> value / 1000
        SECONDS -> value
        MINUTES -> value * 60
        HOURS -> value * 3600
        DAYS -> value * 86400
    }

    public fun toMinutes(value: Double): Double = when (this) {
        MILLISECONDS -> value / 60000
        SECONDS -> value / 60
        MINUTES -> value
        HOURS -> value * 60
        DAYS -> value * 1440
    }

    public fun toHours(value: Double): Double = when (this) {
        MILLISECONDS -> value / 3600000
        SECONDS -> value / 3600
        MINUTES -> value / 60
        HOURS -> value
        DAYS -> value * 24
    }

    public fun toDays(value: Double): Double = when (this) {
        MILLISECONDS -> value / 86400000
        SECONDS -> value / 86400
        MINUTES -> value / 1440
        HOURS -> value / 24
        DAYS -> value
    }

    public fun convert(value: Double, unit: TimeUnit): Double = when (unit) {
        MILLISECONDS -> toMillis(value)
        SECONDS -> toSeconds(value)
        MINUTES -> toMinutes(value)
        HOURS -> toHours(value)
        DAYS -> toDays(value)
    }

}
