package ru.devsand.eventobserver.collection;

import java.time.Duration;
import java.util.Comparator;
import java.util.concurrent.*;

public class SelfServingPriorityQueueStore<E extends Comparable<E>>
        implements AutoCloseableStore<E> {

    private static final int INITIAL_CAPACITY = 11;
    private final BlockingQueue<E> queue;
    private final ScheduledExecutorService executorService;

    public SelfServingPriorityQueueStore(Duration initialDelay, QueueProcessor<E> queueProcessor) {
        this.queue = new PriorityBlockingQueue<>(INITIAL_CAPACITY, Comparator.reverseOrder());
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
    public void close() {
        executorService.shutdownNow();
    }

}
