package ru.devsand.eventregistrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.devsand.eventregistrator.core.EventStore;
import ru.devsand.eventregistrator.core.LeastRecentlyAddedEventsStore;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class BasicEventRegistrator implements EventRegistrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicEventRegistrator.class);

    private EventStore minuteStore;
    private EventStore hourStore;
    private EventStore dayStore;
    private List<EventStore> stores;

    BasicEventRegistrator() {
        minuteStore = new LeastRecentlyAddedEventsStore(Duration.ofMinutes(1));
        hourStore = new LeastRecentlyAddedEventsStore(Duration.ofHours(1));
        dayStore = new LeastRecentlyAddedEventsStore(Duration.ofDays(1));
        stores = Arrays.asList(minuteStore, hourStore, dayStore);
    }

    @Override
    public void registerEvent(LocalDateTime eventTime) {
        Objects.requireNonNull(eventTime);
        long eventMillis = eventTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        stores.forEach(store -> store.add(eventMillis));
        LOGGER.debug("Event added.");
    }

    @Override
    public long getNumberOfEventsInLastMinute() {
        return minuteStore.size();
    }

    @Override
    public long getNumberOfEventsInLastHour() {
        return hourStore.size();
    }

    @Override
    public long getNumberOfEventsInLastDay() {
        return dayStore.size();
    }

    @Override
    public void close() throws Exception {
        for (EventStore store: stores) {
            store.close();
        }
    }

}
