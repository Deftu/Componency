package dev.deftu.componency.time

@JsFun("Date.now()")
private external fun now(): Long

@JsFun("performance.now()")
private external fun performanceNow(): Double

public actual object DefaultClock : Clock {

    actual override fun nowMillis(): Long {
        return now()
    }

    actual override fun nowNanos(): Long {
        return (performanceNow() * 1_000_000).toLong()
    }

}
