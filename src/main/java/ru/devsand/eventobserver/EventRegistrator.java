package ru.devsand.eventobserver;

import java.time.LocalDateTime;

public interface EventRegistrator extends AutoCloseable {

    static EventRegistrator newBasicEventRegistrator() {
        return new BasicEventRegistrator();
    }

    void registerEvent(LocalDateTime eventTime);

    long getNumberOfEventsInLastMinute();

    long getNumberOfEventsInLastHour();

    long getNumberOfEventsInLastDay();

}
