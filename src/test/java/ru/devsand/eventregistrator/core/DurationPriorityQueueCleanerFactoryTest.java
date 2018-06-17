package ru.devsand.eventregistrator.core;

import org.junit.Before;
import org.junit.Test;
import ru.devsand.eventregistrator.collection.QueueProcessor;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class DurationPriorityQueueCleanerFactoryTest {

    private static final int INITIAL_CAPACITY = 11;
    private static final int INITIAL_QUEUE_SIZE = 5;
    private static final long DEFAULT_DURATION = 10L;
    private PriorityBlockingQueue<Long> queue;

    @Before
    public void setUp() throws InterruptedException {
        queue = new PriorityBlockingQueue<>(INITIAL_CAPACITY, Comparator.comparingLong(x -> x));
        for (int i = 0; i < INITIAL_QUEUE_SIZE; i++) {
            queue.add(Instant.now().toEpochMilli());
            TimeUnit.MILLISECONDS.sleep(DEFAULT_DURATION);
        }
    }

    @Test
    public void checkCleanerMustRemoveAtLeastOneDeprecatedItem() throws InterruptedException {
        QueueProcessor<Long, PriorityBlockingQueue<Long>> queueProcessor =
                DurationPriorityQueueCleanerFactory.create(Duration.ofMillis(DEFAULT_DURATION));
        assertThat(queue.size(), equalTo(INITIAL_QUEUE_SIZE));
        TimeUnit.MILLISECONDS.sleep(DEFAULT_DURATION);
        queueProcessor.accept(queue);
        assertThat(queue.size(), lessThan(INITIAL_QUEUE_SIZE));
    }

    @Test
    public void checkValuesInPriorityQueueMustBeSorted() {
        List<Long> items = new ArrayList<>();
        queue.drainTo(items);
        List<Long> sortedItems = new ArrayList<>(items);
        sortedItems.sort(Comparator.comparingLong(x -> x));
        assertThat(items, equalTo(sortedItems));
    }

}