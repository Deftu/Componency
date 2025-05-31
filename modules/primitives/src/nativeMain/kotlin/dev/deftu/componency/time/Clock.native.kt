package dev.deftu.componency.time

public actual object DefaultClock : Clock {

    actual override fun nowMillis(): Long {
        return kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
    }

    actual override fun nowNanos(): Long {
        return kotlinx.datetime.Clock.System.now().toEpochMilliseconds() * 1_000_000
    }

}
