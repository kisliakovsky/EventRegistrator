package ru.devsand.eventregistrator.core;

import ru.devsand.eventregistrator.collection.SelfServingPriorityQueueStore;

import java.time.Duration;

import static ru.devsand.eventregistrator.util.Comparators.longComparator;

public class LeastRecentlyAddedEventsStore extends SelfServingPriorityQueueStore<Long>
        implements EventStore {

    public LeastRecentlyAddedEventsStore(Duration remainTime) {
        super(remainTime, DurationPriorityQueueCleanerFactory.create(remainTime), longComparator());
    }

}
