package ru.devsand.eventregistrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.devsand.eventregistrator.core.EventStore;
import ru.devsand.eventregistrator.core.LeastRecentlyAddedEventsStore;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class BasicEventRegistrator implements EventRegistrator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicEventRegistrator.class);

    private EventStore minuteStore;
    private EventStore hourStore;
    private EventStore dayStore;
    private List<EventStore> stores;
    private ExecutorService readService;

    BasicEventRegistrator() {
        minuteStore = new LeastRecentlyAddedEventsStore(Duration.ofMinutes(1));
        hourStore = new LeastRecentlyAddedEventsStore(Duration.ofHours(1));
        dayStore = new LeastRecentlyAddedEventsStore(Duration.ofDays(1));
        stores = Arrays.asList(minuteStore, hourStore, dayStore);
        readService = Executors.newCachedThreadPool();
    }

    @Override
    public CompletableFuture<Void> registerEvent(LocalDateTime eventTime) {
        Objects.requireNonNull(eventTime);
        return CompletableFuture.runAsync(() -> {
            long eventMillis = eventTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            stores.forEach(store -> store.add(eventMillis));
            LOGGER.debug("Event added.");
        });
    }

    private CompletableFuture<Long> getStoreSize(EventStore store) {
        CompletableFuture<Long> completableFuture = new CompletableFuture<>();
        readService.submit(() -> completableFuture.complete((long) store.size()));
        return completableFuture;
    }

    @Override
    public CompletableFuture<Long> getNumberOfEventsInLastMinute() {
        return getStoreSize(minuteStore);
    }

    @Override
    public CompletableFuture<Long> getNumberOfEventsInLastHour() {
        return getStoreSize(hourStore);
    }

    @Override
    public CompletableFuture<Long> getNumberOfEventsInLastDay() {
        return getStoreSize(dayStore);
    }

    @Override
    public void close() throws Exception {
        for (EventStore store: stores) {
            store.close();
        }
        readService.shutdown();
        try {
            if (!readService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                readService.shutdownNow();
            }
        } catch (InterruptedException e) {
            readService.shutdownNow();
        }
    }

}
