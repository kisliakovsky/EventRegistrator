package ru.devsand.eventregistrator.core;

import ru.devsand.eventregistrator.collection.QueueProcessor;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.PriorityBlockingQueue;

class DurationPriorityQueueCleanerFactory {

    // Suppress default constructor for noninstantiability
    private DurationPriorityQueueCleanerFactory() {
        throw new AssertionError();
    }

    static QueueProcessor<Long, PriorityBlockingQueue<Long>> create(Duration duration) {
        return queue -> {
            if (!queue.isEmpty()) {
                long lastEventMillis = queue.peek();
                long currentMillis = Instant.now().toEpochMilli();
                if (currentMillis - lastEventMillis > duration.toMillis()) {
                    queue.poll();
                }
            }
        };
    }

}
