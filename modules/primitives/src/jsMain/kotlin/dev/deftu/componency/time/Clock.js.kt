package dev.deftu.componency.time

public actual object DefaultClock : Clock {

    actual override fun nowMillis(): Long {
        return js("Date.now()").unsafeCast<Double>().toLong()
    }

}
