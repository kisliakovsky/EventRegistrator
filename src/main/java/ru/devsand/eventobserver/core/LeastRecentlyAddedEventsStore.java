package ru.devsand.eventobserver.core;

import ru.devsand.eventobserver.collection.SelfServingPriorityQueueStore;

import java.time.Duration;

public class LeastRecentlyAddedEventsStore extends SelfServingPriorityQueueStore<Long>
        implements EventStore {

    public LeastRecentlyAddedEventsStore(Duration remainTime) {
        super(remainTime, DurationQueueCleanerFactory.create(remainTime));
    }

}
