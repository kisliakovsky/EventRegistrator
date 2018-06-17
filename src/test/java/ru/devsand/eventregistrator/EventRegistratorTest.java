package ru.devsand.eventregistrator;

import org.awaitility.Duration;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static ru.devsand.eventregistrator.EventRegistrator.newBasicEventRegistrator;

public class EventRegistratorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRegistratorTest.class);

    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int NUMBER_OF_EVENTS_PER_THREAD = 100;
    private static final long TOTAL_NUMBER_OF_EVENTS = NUMBER_OF_THREADS *
            (long) NUMBER_OF_EVENTS_PER_THREAD;
    private EventRegistrator eventRegistrator;

    @Before
    public void setUp() {
        eventRegistrator = newBasicEventRegistrator();
    }

    @Test
    public void checkEventsMustBeAddedFromDifferentThreadsInShortTime() throws InterruptedException {
        final Executor executor = newFixedThreadPool(NUMBER_OF_THREADS);
        long time = MultipleThreadTimer.time(executor, NUMBER_OF_THREADS, () -> {
            try {
                for (int i = 0; i < NUMBER_OF_EVENTS_PER_THREAD; i++) {
                    eventRegistrator.registerEvent(LocalDateTime.now());
                    int delay = ThreadLocalRandom.current().nextInt(1, 4);
                    TimeUnit.MILLISECONDS.sleep(delay);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.error("Failed to add event", e);
            }
        });
        assertThat(eventRegistrator.getNumberOfEventsInLastMinute(), equalTo(TOTAL_NUMBER_OF_EVENTS));
        assertThat(eventRegistrator.getNumberOfEventsInLastDay(), equalTo(TOTAL_NUMBER_OF_EVENTS));
        assertThat(eventRegistrator.getNumberOfEventsInLastHour(), equalTo(TOTAL_NUMBER_OF_EVENTS));
        assertThat(TimeUnit.NANOSECONDS.toMillis(time), lessThan(500L));
        await().atLeast(new Duration(50, SECONDS))
                .atMost(new Duration(70, SECONDS))
                .until(() -> eventRegistrator.getNumberOfEventsInLastMinute(),
                        lessThan(TOTAL_NUMBER_OF_EVENTS));
        await().atMost(new Duration(130, SECONDS))
                .until(() -> eventRegistrator.getNumberOfEventsInLastMinute() == 0);
    }

}