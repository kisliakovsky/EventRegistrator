package ru.devsand.eventregistrator.collection;

import java.time.Duration;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SelfServingPriorityQueueStore<E>
        implements AutoCloseableStore<E> {

    private static final int INITIAL_CAPACITY = 11;
    private final PriorityBlockingQueue<E> queue;
    private final ScheduledExecutorService executorService;

    public SelfServingPriorityQueueStore(
            Duration initialDelay, QueueProcessor<E, PriorityBlockingQueue<E>> queueProcessor,
            Comparator<E> comparator) {
        this.queue = new PriorityBlockingQueue<>(INITIAL_CAPACITY, comparator);
        this.executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> queueProcessor.accept(queue),
                initialDelay.toMillis(), 1, TimeUnit.MILLISECONDS);
    }

    @Override
    public void add(E item) {
        queue.offer(item);
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public void close() {
        executorService.shutdownNow();
    }

}
