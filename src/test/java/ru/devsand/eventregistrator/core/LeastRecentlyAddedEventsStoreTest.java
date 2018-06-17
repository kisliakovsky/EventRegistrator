package ru.devsand.eventregistrator.core;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class LeastRecentlyAddedEventsStoreTest {

    private static final int INITIAL_QUEUE_SIZE = 5;
    private static final long DEFAULT_DURATION = 10L;

    private EventStore store;

    @Before
    public void setUp() throws InterruptedException {
        store = new LeastRecentlyAddedEventsStore(Duration.ofSeconds(1));
        for (int i = 0; i < INITIAL_QUEUE_SIZE; i++) {
            store.add(Instant.now().toEpochMilli());
            TimeUnit.MILLISECONDS.sleep(DEFAULT_DURATION);
        }
    }

    @Test
    public void checkStoreMustBeFullWhileServiceDelayed() {
        assertThat(store.size(), equalTo(INITIAL_QUEUE_SIZE));
    }

    @Test
    public void checkStoreMustBeEmptyAfterCertainTime() {
        await().atLeast(org.awaitility.Duration.ONE_SECOND)
                .atMost(org.awaitility.Duration.FIVE_SECONDS)
                .until(store::isEmpty);
    }

}