package ru.devsand.eventregistrator.collection;

import org.junit.Test;
import ru.devsand.eventregistrator.util.TimeUtil;

import java.time.Duration;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static ru.devsand.eventregistrator.util.Comparators.longComparator;

public class SelfServingPriorityQueueStoreTest {

    @Test
    public void checkMinValuesMustBeRemoved() throws InterruptedException {
        long min = 1L;
        long max = 10L;
        long mean = (max - min) / 2;
        Predicate<Long> lessThanMean = value -> value < mean;
        QueueProcessor<Long, PriorityBlockingQueue<Long>> cleanLessThanMeanValues = queue -> {
            if (!queue.isEmpty() && lessThanMean.test(queue.peek())) {
                queue.poll();
            }
        };
        long servingDelay = 1;
        TimeUnit servingDelayTimeUnit = SECONDS;
        Duration servingDelayDuration =
                Duration.of(1, TimeUtil.toChronoUnit(servingDelayTimeUnit));
        try(SelfServingPriorityQueueStore<Long> store = new SelfServingPriorityQueueStore<>(
                servingDelayDuration, cleanLessThanMeanValues, longComparator())) {
            int n = 3;
            for (int i = 0; i < n; i++) {
                store.add(min);
                store.add(max);
            }
            assertThat(store.size(), equalTo(n * 2));
            servingDelayTimeUnit.sleep(servingDelay + 1L);
            assertThat(store.size(), equalTo(n));
        }
    }

}