package ru.devsand.eventobserver;

import ru.devsand.eventobserver.core.EventStore;
import ru.devsand.eventobserver.core.LeastRecentlyAddedEventsStore;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

class BasicEventRegistrator implements EventRegistrator {

    private EventStore secondStore;
    private EventStore hourStore;
    private EventStore dayStore;
    private List<EventStore> stores;

    BasicEventRegistrator() {
        secondStore = new LeastRecentlyAddedEventsStore(Duration.ofSeconds(1));
        hourStore = new LeastRecentlyAddedEventsStore(Duration.ofHours(1));
        dayStore = new LeastRecentlyAddedEventsStore(Duration.ofDays(1));
        stores = Arrays.asList(secondStore, hourStore, dayStore);
    }

    @Override
    public void registerEvent(LocalDateTime eventTime) {
        long eventMillis = eventTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        stores.forEach(store -> store.add(eventMillis));
    }

    @Override
    public long getNumberOfEventsInLastMinute() {
        return secondStore.size();
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
