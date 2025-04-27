package dev.deftu.componency.time

public actual object DefaultClock : Clock {

    actual override fun nowMillis(): Long {
        return kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
    }

}
