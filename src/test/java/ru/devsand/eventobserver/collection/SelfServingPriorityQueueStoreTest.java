package ru.devsand.eventobserver.collection;

import org.junit.Test;
import ru.devsand.eventobserver.util.TimeUtil;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class SelfServingPriorityQueueStoreTest {

    @Test
    public void checkCustomSelfServing() throws InterruptedException {
        long min = 1L;
        long max = 10L;
        long mean = (max - min) / 2;
        Predicate<Long> moreThanMean = value -> value > mean;
        QueueProcessor<Long> cleanMoreThanMeanValues = (queue) -> {
            if (!queue.isEmpty()) {
                if (moreThanMean.test(queue.peek())) {
                    queue.poll();
                }
            }
        };
        long servingDelay = 1;
        TimeUnit servingDelayTimeUnit = SECONDS;
        Duration servingDelayDuration = Duration.of(1, TimeUtil.toChronoUnit(servingDelayTimeUnit));
        SelfServingPriorityQueueStore<Long> store = new SelfServingPriorityQueueStore<>(
                servingDelayDuration, cleanMoreThanMeanValues);
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