package dev.deftu.componency.time

public actual object DefaultClock : Clock {

    actual override fun nowMillis(): Long {
        return js("Date.now()").unsafeCast<Double>().toLong()
    }

    actual override fun nowNanos(): Long {
        return (js("performance.now()").unsafeCast<Double>() * 1_000_000.0).toLong()
    }

}
