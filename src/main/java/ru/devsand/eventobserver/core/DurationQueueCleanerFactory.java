package ru.devsand.eventobserver.core;

import ru.devsand.eventobserver.collection.QueueProcessor;

import java.time.Duration;
import java.time.Instant;

class DurationQueueCleanerFactory {

    // Suppress default constructor for noninstantiability
    private DurationQueueCleanerFactory() {
        throw new AssertionError();
    }

    static QueueProcessor<Long> create(Duration duration) {
        return queue -> {
            if (!queue.isEmpty()) {
                long lastEventMillis = queue.peek();
                long currentMillis = Instant.now().toEpochMilli();
                if (currentMillis - lastEventMillis < duration.toMillis()) {
                    queue.poll();
                }
            }
        };
    }

}
