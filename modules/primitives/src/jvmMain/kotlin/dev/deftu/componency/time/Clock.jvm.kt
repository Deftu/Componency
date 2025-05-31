package dev.deftu.componency.time

public actual object DefaultClock : Clock {

    actual override fun nowMillis(): Long {
        return System.currentTimeMillis()
    }

    actual override fun nowNanos(): Long {
        return System.nanoTime()
    }

}
