package ru.devsand.eventregistrator;

import java.time.LocalDateTime;

/**
 * Basic events' statistics collector. For example, the user of this interface can monitor
 * publications of posts (messages) in some news feed.
 */
public interface EventRegistrator extends AutoCloseable {

    /**
     * Creates an EventRegistrator that uses self-cleaning event stores. Old events are
     * removed when the default period expired. Basic implementation uses three stores
     * with one minute, hour and day period respectively.
     * @return the newly created event registrator
     */
    static EventRegistrator newBasicEventRegistrator() {
        return new BasicEventRegistrator();
    }

    /**
     * Allows including event in collecting statistics.
     * @param eventTime - the time when the event occurs
     * @throws NullPointerException - if eventTime is null
     */
    void registerEvent(LocalDateTime eventTime);

    /**
     * Returns the number of events that occurred in the last minute.
     * @return the number of events that occurred in the last minute
     */
    long getNumberOfEventsInLastMinute();

    /**
     * Returns the number of events that occurred in the last hour.
     * @return the number of events that occurred in the last hour
     */
    long getNumberOfEventsInLastHour();

    /**
     * Returns the number of events that occurred in last twenty four hours.
     * @return the number of events that occurred in last twenty four hours
     */
    long getNumberOfEventsInLastDay();

}
