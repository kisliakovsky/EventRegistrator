package ru.devsand.eventobserver.util;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    // Suppress default constructor for noninstantiability
    private TimeUtil() {
        throw new AssertionError();
    }

    public static ChronoUnit toChronoUnit(TimeUnit timeUnit) {
        switch (timeUnit) {
            case NANOSECONDS:  return ChronoUnit.NANOS;
            case MICROSECONDS: return ChronoUnit.MICROS;
            case MILLISECONDS: return ChronoUnit.MILLIS;
            case SECONDS:      return ChronoUnit.SECONDS;
            case MINUTES:      return ChronoUnit.MINUTES;
            case HOURS:        return ChronoUnit.HOURS;
            case DAYS:         return ChronoUnit.DAYS;
            default: throw new AssertionError();
        }
    }

}
