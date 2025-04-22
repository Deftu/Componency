package dev.deftu.componency.time

@JsFun("Date.now()")
private external fun now(): Long

public actual object DefaultClock : Clock {

    actual override fun nowMillis(): Long {
        return now()
    }

}
